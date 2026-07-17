/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters.events;

import cart.ai.shopping.domain.model.shop.events.TranslationRequestedEvent;
import cart.ai.shopping.domain.ports.shop.TranslationEventPublisherPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TranslationEventPublisherOutboxTransactionAdapter implements TranslationEventPublisherPort {

    private static final String TRANSLATION_REQUESTED_TOPIC = "translation.requested";
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishTranslationRequested(TranslationRequestedEvent event) {
        saveOutbox(event.getProductId(), event, TRANSLATION_REQUESTED_TOPIC);
    }

    private void saveOutbox(String aggregateId, Object event, String topic) {
        try {
            OutboxTransactionDocument outboxTransactionDocument = OutboxTransactionDocument.builder()
                    .aggregateType(event.getClass().getName())
                    .aggregateId(aggregateId)
                    .key(aggregateId)
                    .topic(topic)
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxTransactionDocument.PENDING)
                    .createdDate(new Date())
                    .build();

            mongoTemplate.save(outboxTransactionDocument);
        } catch (Exception exception) {
            log.error("Failed to save outbox event for topic {}: {}", topic, exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }
}
