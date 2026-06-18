package com.bikemmerce.commerce.domain.ports.shop;

import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;

import java.util.List;

public interface CartRepositoryPort {

    void delete(UserId userId);

    Cart find(UserId userId);

    List<Cart> findAll();

    Cart save(Cart cart);
}
