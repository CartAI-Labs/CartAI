package com.bikemmerce.commerce.domain.ports.shop.events;

import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderPlacedEvent;

public interface OrderPlacedEventPublisherPort {
    void publish(OrderPlacedEvent event);
}
