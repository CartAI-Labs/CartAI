package cart.ai.domain.ports.shop.repositories;

import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Cart;

import java.util.List;

public interface CartRepositoryPort {

    void delete(UserId userId);

    Cart find(UserId userId);

    List<Cart> findAll();

    Cart save(Cart cart);
}
