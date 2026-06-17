package com.bikemmerce.commerce.infrastructure.out.kafka;

import com.bikemmerce.commerce.domain.model.value.objects.CustomerAddedEvent;
import com.bikemmerce.commerce.domain.ports.events.CustomerAddedEventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerAddedEventPublisherKafkaAdapter implements CustomerAddedEventPublisherPort {

    private static final String TOPIC = "customers-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void added(CustomerAddedEvent event) {
        kafkaTemplate.send(TOPIC, event.customerId().value(), event);
    }
}
