package com.bikemmerce.commerce.infrastructure.in.rest.mapper;

import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.order.OrderRestResponse;

public class OrderRestMapper {

    public static OrderRestResponse toResponse(Order order) {
        return new OrderRestResponse(
                order.getOrderId().value(), order.getUserId().value(), order.getShoppingItems(),
                order.getStatus().getValue(), order.getCreateDate());
    }
}
