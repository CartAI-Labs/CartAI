package com.bikemmerce.commerce.domain.ports.shop.repositories;

import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Customer;

import java.util.List;

public interface CustomerRepositoryPort {

    void delete(UserId userId);

    Customer findByCustomerId(UserId userId);

    Customer findByEmail(Email email);

    List<Customer> findAll();

    Customer save(Customer product);

}