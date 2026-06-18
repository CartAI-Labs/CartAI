package com.bikemmerce.commerce.application.usecases.shop.cart;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.shop.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.shop.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class RemoveShoppingItemFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public Result<Cart> execute(UserId userId, ProductId productId) {
        Cart cart = cartRepositoryPort.find(userId);

        if (cart == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Product product = productRepositoryPort.find(productId);

        if (product == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        cart.removeItem(productId);

        return Result.success(cartRepositoryPort.save(cart));
    }
}