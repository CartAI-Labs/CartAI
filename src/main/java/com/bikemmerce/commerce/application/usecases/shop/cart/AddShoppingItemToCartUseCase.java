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

import java.util.ArrayList;

@RequiredArgsConstructor
@UseCase
public class AddShoppingItemToCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public Result<Cart> execute(UserId userId, ProductId productId) {
        Product product = productRepositoryPort.find(productId);

        if (product == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Cart cart = cartRepositoryPort.find(userId);

        if (cart == null) {
            cart = new Cart(userId, new ArrayList<>());
        }

        cart.addItem(product, 1);

        return Result.success(cartRepositoryPort.save(cart));
    }
}