package cart.ai.infrastructure.in.rest.dto.cart;

import cart.ai.domain.model.shop.value.objects.ShoppingItem;

import java.util.List;

public record CartRestResponse(
        String customerId,
        List<ShoppingItem> shoppingItems
) {
}
