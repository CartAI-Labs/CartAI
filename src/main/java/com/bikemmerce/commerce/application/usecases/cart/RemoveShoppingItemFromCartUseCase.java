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

@RequiredArgsConstructor
public class RemoveShoppingItemFromCartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public Result<Cart> execute(String customerId, String id) {
        Cart cart = cartRepositoryPort.find(new CustomerId(customerId));

        if (cart == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        ProductId productId = new ProductId(id);

        Product product = productRepositoryPort.find(productId);

        if (product == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        cart.removeItem(productId);

        return Result.success(cartRepositoryPort.save(cart));
    }
}