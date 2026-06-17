package com.bikemmerce.commerce.domain.model.value.objects;

import com.bikemmerce.commerce.domain.model.constants.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

public record OrderPlacedEvent(
        OrderId orderId,
        CustomerId customerId,
        BigDecimal totalPrice,
        OrderStatus orderStatus,
        Date createDate
) {
}