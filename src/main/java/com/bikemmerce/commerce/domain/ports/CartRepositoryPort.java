package com.bikemmerce.commerce.domain.ports;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;

import java.util.List;

public interface CartRepositoryPort {

    void delete(CustomerId customerId);

    Cart find(CustomerId customerId);

    List<Cart> findAll();

    Cart save(Cart cart);
}
