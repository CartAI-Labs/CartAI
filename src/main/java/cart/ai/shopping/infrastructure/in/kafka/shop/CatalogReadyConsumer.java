/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.shop;

import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.application.usecases.shop.product.CreateProductUseCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Listens to the catalog.ready topic published by the Python AI Worker using reactor-kafka.
 *
 * @author Roberto Díaz
 */
@Component
@Slf4j
public class CatalogReadyConsumer {

    private final CreateProductUseCase createProductUseCase;
    private final ObjectMapper objectMapper;
    private final KafkaReceiver<String, String> kafkaReceiver;

    public CatalogReadyConsumer(
            CreateProductUseCase createProductUseCase,
            ObjectMapper objectMapper,
            ReceiverOptions<String, String> receiverOptions) {
        
        this.createProductUseCase = createProductUseCase;
        this.objectMapper = objectMapper;
        
        // Configure topic subscription and specific properties for this listener
        ReceiverOptions<String, String> options = receiverOptions
                .subscription(List.of("catalog.ready"))
                .consumerProperty(ConsumerConfig.GROUP_ID_CONFIG, "cartai-java-backend");

        this.kafkaReceiver = KafkaReceiver.create(options);
    }

    @PostConstruct
    public void startConsuming() {
        log.info("Starting Reactive Kafka Consumer for topic: catalog.ready");
        
        kafkaReceiver.receive()
                .flatMap(receiverRecord -> {
                    String message = receiverRecord.value();
                    log.info("Received new product from catalog AI extraction: {}", message);
                    
                    return Mono.fromCallable(() -> {
                        // 1. Parse JSON
                        CatalogProductEvent event = objectMapper.readValue(message, CatalogProductEvent.class);
                        
                        // 2. Map the AI extracted schema to our domain command
                        String name = event.getName();
                        if (event.getBrand() != null && !event.getBrand().isBlank()) {
                            name = event.getBrand() + " " + name;
                        }

                        Map<String, String> attributes = null;
                        if (event.getAttributes() != null) {
                            attributes = event.getAttributes().entrySet().stream()
                                    .collect(Collectors.toMap(
                                            Map.Entry::getKey,
                                            e -> String.valueOf(e.getValue())
                                    ));
                        }

                        String description = "Category: " + event.getCategory();

                        BigDecimal price = event.getPrice() != null ? BigDecimal.valueOf(event.getPrice()) : BigDecimal.ZERO;
                        Integer defaultStock = 10;

                        CreateProductCommand command = new CreateProductCommand(
                                name,
                                description,
                                price,
                                defaultStock,
                                Collections.emptyList(),
                                attributes
                        );

                        // 3. Execute synchronous domain logic (writes to MongoDB)
                        createProductUseCase.execute(command);
                        log.info("Product successfully ingested into the catalog: {}", name);
                        return true;
                    })
                    // Crucial: Offload the blocking domain logic to an elastic thread pool 
                    // so we don't block the reactive Netty Event Loop
                    .subscribeOn(Schedulers.boundedElastic())
                    // Acknowledge the offset only when everything succeeds
                    .doOnSuccess(ignored -> receiverRecord.receiverOffset().acknowledge())
                    // If an error occurs, log it and return empty to continue with the next message
                    .onErrorResume(e -> {
                        log.error("Error processing catalog.ready message: {}", e.getMessage(), e);
                        return Mono.empty();
                    });
                })
                .subscribe(); // Start the reactive pipeline
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
        private Double confidenceScore;
        private Boolean needsHumanReview;
    }
}
