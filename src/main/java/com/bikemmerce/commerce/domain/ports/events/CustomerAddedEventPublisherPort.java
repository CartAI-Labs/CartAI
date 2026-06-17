package com.bikemmerce.commerce.domain.ports.events;

import com.bikemmerce.commerce.domain.model.value.objects.CustomerAddedEvent;

public interface CustomerAddedEventPublisherPort {
    void added(CustomerAddedEvent event);
}
