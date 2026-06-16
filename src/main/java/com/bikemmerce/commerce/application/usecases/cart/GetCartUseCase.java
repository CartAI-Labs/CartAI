package com.bikemmerce.commerce.application.usecases.cart;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class GetCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    public Result<Cart> execute(String id) {
        Cart cart = cartRepositoryPort.find(new CustomerId(id));

        if (cart == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        return Result.success(cart);
    }

}