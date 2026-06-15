package com.bikemmerce.commerce.domain.ports;

import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;

import java.util.List;

public interface CustomerRepositoryPort {

    void delete(CustomerId customerId);

    Customer findByCustomerId(CustomerId customerId);

    Customer findByEmail(Email email);

    List<Customer> findAll();

    Customer save(Customer product);

}