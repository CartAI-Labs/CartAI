package com.bikemmerce.commerce.application.usecases.shop.cart;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.ports.shop.CartRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class ClearCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;

    public Result<Cart> execute(UserId userId) {
        Cart cart = cartRepositoryPort.find(userId);

        if (cart != null) {
            cart.clearItems();

            return Result.success(cartRepositoryPort.save(cart));
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}