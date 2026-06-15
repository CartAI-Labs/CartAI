package com.bikemmerce.commerce.application.usecases.cart;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

public class ClearCartUseCase {

    private CartRepositoryPort cartRepositoryPort;

    public Result<Cart> execute(CustomerId customerId) {
        Cart cart = cartRepositoryPort.find(customerId);

        if (cart != null) {
            cart.setShoppingItems(new ArrayList<>());

            return Result.success(cartRepositoryPort.save(cart));
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}