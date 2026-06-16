package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.cart.AddShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.cart.CartRestResponse;
import com.bikemmerce.commerce.adapters.in.rest.dto.cart.RemoveShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.order.OrderRestResponse;
import com.bikemmerce.commerce.application.usecases.commands.AddShoppingItemToCartCommand;
import com.bikemmerce.commerce.application.usecases.commands.RemoveShoppingItemToCartCommand;
import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.Order;

public class OrderRestMapper {

    public static OrderRestResponse toResponse(Order order) {
        return new OrderRestResponse(
                order.getOrderId(), order.getCustomerId(), order.getShoppingItems(), order.getStatus(), order.getCreateDate());
    }
}
