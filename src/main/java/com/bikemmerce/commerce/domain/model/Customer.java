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
    private Email email;
    @NonNull
    private String name;

}