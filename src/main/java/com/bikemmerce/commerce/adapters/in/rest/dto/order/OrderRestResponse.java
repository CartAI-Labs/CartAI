package com.bikemmerce.commerce.adapters.in.rest.dto.order;

import com.bikemmerce.commerce.domain.model.ShoppingItem;
import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

public record OrderRestResponse(
        OrderId orderId,
        CustomerId customerId,
        List<ShoppingItem> shoppingItems,
        OrderStatus status,
        Date createDate
) {
}
