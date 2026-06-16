package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.order.OrderRestResponse;
import com.bikemmerce.commerce.domain.model.Order;

public class OrderRestMapper {

    public static OrderRestResponse toResponse(Order order) {
        return new OrderRestResponse(
                order.getOrderId(), order.getCustomerId(), order.getShoppingItems(), order.getStatus(), order.getCreateDate());
    }
}
