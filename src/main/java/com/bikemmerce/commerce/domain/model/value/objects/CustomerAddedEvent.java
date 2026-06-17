package com.bikemmerce.commerce.domain.model.value.objects;

public record CustomerAddedEvent(
        CustomerId customerId,
        String name,
        Email email
) {
}