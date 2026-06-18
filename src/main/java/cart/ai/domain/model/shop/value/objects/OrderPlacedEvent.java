package cart.ai.domain.model.shop.value.objects;

import cart.ai.domain.model.constants.OrderStatus;
import cart.ai.domain.model.security.value.objects.UserId;

import java.math.BigDecimal;
import java.util.Date;

public record OrderPlacedEvent(
        OrderId orderId,
        UserId userId,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        Date createDate
) {
}