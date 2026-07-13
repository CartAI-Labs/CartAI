/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.bootstrap.initializers;

import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Initializer to seed default shop products for development if the database is empty.
 *
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "cartai.bootstrap", name = "enabled", havingValue = "true")
public class ProductInitializer {

    private final ProductRepositoryPort productRepositoryPort;
    private final IncrementIdGeneratorPort idGeneratorPort;

    @EventListener(ApplicationReadyEvent.class)
    @Order(4)
    public void initializeProducts() {
        if (productRepositoryPort.findAll().isEmpty()) {
            log.info("Product catalog is empty. Seeding mock products...");

            createProduct("Cart•AI Smart Terminal", 
                    "Terminal inteligente de punto de venta con recomendaciones por IA en tiempo real y pantalla táctil HD.", 
                    new BigDecimal("299.99"), 5,
                    Map.of(
                        "Pantalla", "15.6 pulgadas HD",
                        "Procesador", "ARM Cortex-A76",
                        "Conectividad", "Wi-Fi 6, 5G",
                        "Batería", "8000 mAh"
                    ));

            createProduct("Predictive Stock Tracker", 
                    "Sensor IoT ultrapreciso para estanterías que predice la rotura de stock y automatiza pedidos.", 
                    new BigDecimal("149.50"), 3, null);

            createProduct("Automated Cart Tag", 
                    "Etiqueta digital inteligente de tinta electrónica para carritos de compra que sincroniza precios dinámicos.", 
                    new BigDecimal("19.99"), 15, null);

            createProduct("Hexagonal Hub Gateway", 
                    "Servidor de comunicación local con arquitectura hexagonal redundante y encriptación de grado militar.", 
                    new BigDecimal("599.00"), 2, null);

            createProduct("Hexagonal Hub Gateway 2", 
                    "Servidor de comunicación remoto con arquitectura hexagonal redundante y encriptación de grado militar.", 
                    new BigDecimal("1000.00"), 0, null);

            log.info("Mock products seeded successfully.");
        }
    }

    private void createProduct(String name, String description, BigDecimal price, Integer stock, Map<String, String> attributes) {
        String idStr = idGeneratorPort.generate(Product.class);
        ProductId id = new ProductId(idStr);
        Map<String, String> finalAttributes = attributes != null ? attributes : Collections.emptyMap();
        Product product = new Product(id, name, description, price, stock, List.of(), finalAttributes);
        productRepositoryPort.save(product);
        log.info("Seeded product: {} (ID: {})", name, idStr);
    }
}
