package cart.ai.application.usecases.shop.cart;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Cart;
import cart.ai.domain.model.shop.Product;
import cart.ai.domain.model.shop.value.objects.ProductId;
import cart.ai.domain.ports.shop.repositories.CartRepositoryPort;
import cart.ai.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.domain.result.Result;
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