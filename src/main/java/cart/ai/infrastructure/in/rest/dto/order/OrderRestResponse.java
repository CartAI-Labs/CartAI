package cart.ai.infrastructure.in.rest.dto.order;

import cart.ai.domain.model.shop.value.objects.ShoppingItem;

import java.util.Date;
import java.util.List;

public record OrderRestResponse(
        String orderId,
        String customerId,
        List<ShoppingItem> shoppingItems,
        String status,
        Date createDate
) {
}
