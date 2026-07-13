/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.shop;

import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.application.usecases.shop.product.CreateProductUseCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Listens to the catalog.ready topic published by the Python AI Worker.
 *
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogReadyListener {

    private final CreateProductUseCase createProductUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "catalog.ready",
            groupId = "cartai-java-backend",
            properties = {"value.deserializer=org.apache.kafka.common.serialization.StringDeserializer"}
    )
    public void onCatalogReady(String message) {
        log.info("Received new product from catalog AI extraction: {}", message);
        try {
            CatalogProductEvent event = objectMapper.readValue(message, CatalogProductEvent.class);
            
            // Map the AI extracted schema to our domain command
            String name = event.getName();
            if (event.getBrand() != null && !event.getBrand().isBlank()) {
                name = event.getBrand() + " " + name;
            }

            Map<String, String> attributes = null;
            if (event.getAttributes() != null) {
                attributes = event.getAttributes().entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> String.valueOf(e.getValue())
                        ));
            }

            String description = "Category: " + event.getCategory();

            BigDecimal price = event.getPrice() != null ? BigDecimal.valueOf(event.getPrice()) : BigDecimal.ZERO;
            Integer defaultStock = 10; // Default stock for newly extracted catalog items

            CreateProductCommand command = new CreateProductCommand(
                    name,
                    description,
                    price,
                    defaultStock,
                    Collections.emptyList(),
                    attributes
            );

            createProductUseCase.execute(command);
            log.info("Product successfully ingested into the catalog: {}", name);

        } catch (Exception e) {
            log.error("Error processing catalog.ready message: {}", e.getMessage(), e);
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CatalogProductEvent {
        private String sku;
        private String name;
        private String category;
        private String brand;
        private Double price;
        private String currency;
        private Map<String, Object> attributes;
        private Double confidence_score;
        private Boolean needs_human_review;
    }
}
