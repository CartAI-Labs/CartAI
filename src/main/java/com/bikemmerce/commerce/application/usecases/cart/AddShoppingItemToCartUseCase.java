package com.bikemmerce.commerce.application.usecases.cart;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@RequiredArgsConstructor
public class AddShoppingItemToCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public Result<Cart> execute(String id, ProductId productId) {
        Product product = productRepositoryPort.find(productId);

        if (product == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Cart cart = cartRepositoryPort.find(new CustomerId(id));

        if (cart == null) {
            cart = new Cart(new CustomerId(id), new ArrayList<>());
        }

        cart.addItem(product, 1);

        return Result.success(cartRepositoryPort.save(cart));
    }
}