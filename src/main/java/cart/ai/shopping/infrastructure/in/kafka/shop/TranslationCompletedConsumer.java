/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.shop;

import cart.ai.shopping.application.usecases.shop.product.UpdateProductTranslationsUseCase;
import cart.ai.shopping.domain.model.shop.events.TranslationCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.List;

/**
 * Listens to the translation.completed topic published by the Go ProductTranslator worker.
 *
 * @author Roberto Díaz
 */
@Component
@Slf4j
public class TranslationCompletedConsumer {

    private final UpdateProductTranslationsUseCase updateProductTranslationsUseCase;
    private final ObjectMapper objectMapper;
    private final KafkaReceiver<String, String> kafkaReceiver;

    public TranslationCompletedConsumer(
            UpdateProductTranslationsUseCase updateProductTranslationsUseCase,
            ObjectMapper objectMapper,
            ReceiverOptions<String, String> receiverOptions) {

        this.updateProductTranslationsUseCase = updateProductTranslationsUseCase;
        this.objectMapper = objectMapper;

        // Configure topic subscription
        ReceiverOptions<String, String> options = receiverOptions
                .subscription(List.of("translation.completed"))
                .consumerProperty(ConsumerConfig.GROUP_ID_CONFIG, "cartai-translation-completed");

        this.kafkaReceiver = KafkaReceiver.create(options);
    }

    @PostConstruct
    public void startConsuming() {
        log.info("Starting Reactive Kafka Consumer for topic: translation.completed");

        kafkaReceiver.receive()
                .flatMap(receiverRecord -> {
                    String message = receiverRecord.value();
                    log.info("Received completed translations: {}", message);

                    return Mono.fromCallable(() -> {
                                // 1. Parse JSON
                                TranslationCompletedEvent event = objectMapper.readValue(message, TranslationCompletedEvent.class);

                                // 2. Execute synchronous domain logic (writes to MongoDB)
                                var result = updateProductTranslationsUseCase.execute(event);

                                if (result.hasError()) {
                                    log.error(
                                            "Failed to update translations for product {}: {}",
                                            event.getProductId(), result.getError());
                                }

                                return true;
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .doOnSuccess(ignored -> receiverRecord.receiverOffset().acknowledge())
                            .onErrorResume(e -> {
                                log.error("Error processing translation.completed message: {}", e.getMessage(), e);
                                return Mono.empty();
                            });
                })
                .subscribe(); // Start the reactive pipeline
    }
}
