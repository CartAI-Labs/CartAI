package com.bikemmerce.commerce.domain.ports.shop.events;

import com.bikemmerce.commerce.domain.model.shop.value.objects.CustomerAddedEvent;

public interface CustomerAddedEventPublisherPort {
    void added(CustomerAddedEvent event);
}
