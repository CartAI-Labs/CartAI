package com.bikemmerce.commerce.domain.model;

import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import lombok.Data;
import lombok.NonNull;

@Data
public class Customer {

    @NonNull
    private CustomerId customerId;
    @NonNull
    private String name;
    @NonNull
    private Email email;

    public Customer(@NonNull CustomerId customerId, @NonNull String name, @NonNull Email email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }
}