package com.bikemmerce.commerce.domain.model.shop.value.objects;

import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;

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