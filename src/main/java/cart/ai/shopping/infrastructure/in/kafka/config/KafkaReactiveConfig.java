/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Map;

/**
 * @author Roberto Díaz
 */
/**
 * Configuration for Reactive Kafka using reactor-kafka.
 *
 * @author Roberto Díaz
 */
@Configuration
public class KafkaReactiveConfig {

    @Bean
    public ReceiverOptions<String, String> kafkaReceiverOptions(KafkaProperties kafkaProperties) {
        // Build base consumer properties using Spring Boot's configuration
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties(null);
        
        // Ensure StringDeserializer is used for both keys and values as required by reactor-kafka flows,
        // overriding the global JsonDeserializer set in application.properties for standard @KafkaListeners
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        
        // For reactive consumers, it's safer to disable auto-commit and commit explicitly after processing
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return ReceiverOptions.create(consumerProps);
    }
}
