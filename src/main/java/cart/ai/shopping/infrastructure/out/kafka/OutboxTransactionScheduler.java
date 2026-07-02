/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.kafka;

import cart.ai.shopping.infrastructure.out.persistence.mongo.common.documents.OutboxTransactionDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Roberto Díaz
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxTransactionScheduler {

    private static final long STUCK_THRESHOLD_MINUTES = 5;

    private final MongoTemplate mongoTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000)
    public void processOutbox() {
        Query query = new Query(Criteria.where("status")
                .in(OutboxTransactionDocument.PENDING, OutboxTransactionDocument.FAIL)
                .andOperator(Criteria.where("retryCount").lt(OutboxTransactionDocument.MAX_RETRIES)));

        Update update = Update.update("status", OutboxTransactionDocument.PROCESSING)
                .set("lastAttemptDate", new Date())
                .inc("retryCount", 1);

        OutboxTransactionDocument event;

        while ((event = mongoTemplate.findAndModify(query, update, OutboxTransactionDocument.class)) != null) {
            final OutboxTransactionDocument curEvent = event;

            Object parsedPayload;
            try {
                Class<?> clazz = Class.forName(event.getAggregateType());
                parsedPayload = objectMapper.readValue(event.getPayload(), clazz);
            } catch (Exception e) {
                // Fallback to JsonNode for backward compatibility or when class is missing
                try {
                    parsedPayload = objectMapper.readTree(event.getPayload());
                } catch (Exception ex) {
                    log.error("Failed to parse outbox payload for event {}", event.getId(), ex);
                    updateStatus(event.getId(), OutboxTransactionDocument.FAIL);
                    continue;
                }
            }

            kafkaTemplate.send(event.getTopic(), event.getKey(), parsedPayload)
                    .whenComplete((result, throwable) -> {
                        if (throwable == null) {
                            updateStatus(curEvent.getId(), OutboxTransactionDocument.SUCCESS);
                        } else {
                            log.error("Error handling outbox event {} (attempt {})",
                                    curEvent.getId(), curEvent.getRetryCount() + 1, throwable);
                            updateStatus(curEvent.getId(), OutboxTransactionDocument.FAIL);
                        }
                    });
        }

        // Mark events that exhausted retries as DEAD
        Query exhaustedQuery = new Query(Criteria.where("status")
                .is(OutboxTransactionDocument.FAIL)
                .and("retryCount").gte(OutboxTransactionDocument.MAX_RETRIES));

        Update deadUpdate = Update.update("status", OutboxTransactionDocument.DEAD);
        mongoTemplate.updateMulti(exhaustedQuery, deadUpdate, OutboxTransactionDocument.class);
    }

    @Scheduled(fixedDelay = 60000)
    public void recoverStuckEvents() {
        Date threshold = Date.from(Instant.now().minus(STUCK_THRESHOLD_MINUTES, ChronoUnit.MINUTES));

        Query stuckQuery = new Query(Criteria.where("status")
                .is(OutboxTransactionDocument.PROCESSING)
                .and("lastAttemptDate").lt(threshold));

        Update resetUpdate = Update.update("status", OutboxTransactionDocument.PENDING);

        long count = mongoTemplate.updateMulti(stuckQuery, resetUpdate, OutboxTransactionDocument.class).getModifiedCount();

        if (count > 0) {
            log.warn("Recovered {} stuck outbox events back to PENDING", count);
        }
    }

    private void updateStatus(String eventId, int status) {
        Query updateQuery = new Query(Criteria.where("id").is(eventId));
        mongoTemplate.updateFirst(updateQuery,
                Update.update("status", status), OutboxTransactionDocument.class);
    }
}

