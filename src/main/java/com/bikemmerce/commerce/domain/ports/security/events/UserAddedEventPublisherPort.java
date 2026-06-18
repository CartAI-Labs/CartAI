package com.bikemmerce.commerce.domain.ports.security.events;

import com.bikemmerce.commerce.domain.model.security.value.objects.UserAddedEvent;

public interface UserAddedEventPublisherPort {
    void added(UserAddedEvent event);
}
