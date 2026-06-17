package com.bikemmerce.commerce.domain.ports.events;

import com.bikemmerce.commerce.domain.model.value.objects.OrderPlacedEvent;

public interface OrderPlacedEventPublisherPort {
    void publish(OrderPlacedEvent event);
}
