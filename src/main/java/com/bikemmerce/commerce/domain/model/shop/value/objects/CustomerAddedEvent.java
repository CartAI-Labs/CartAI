package com.bikemmerce.commerce.domain.model.shop.value.objects;

import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;

public record CustomerAddedEvent(
        UserId userId,
        String name,
        Email email
) {
}