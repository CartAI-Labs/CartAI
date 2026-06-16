package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.cart.AddShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.cart.CartRestResponse;
import com.bikemmerce.commerce.adapters.in.rest.dto.cart.RemoveShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.application.usecases.commands.AddShoppingItemToCartCommand;
import com.bikemmerce.commerce.application.usecases.commands.RemoveShoppingItemToCartCommand;
import com.bikemmerce.commerce.domain.model.Cart;

public class CartRestMapper {

    public static AddShoppingItemToCartCommand toAddShoppingItemToCartCommand(AddShoppingItemToCartRestRequest request) {
        return new AddShoppingItemToCartCommand(request.customerId(), request.productId(), request.quantity());
    }


    public static RemoveShoppingItemToCartCommand toRemoveShoppingItemToCartCommand(RemoveShoppingItemToCartRestRequest request) {
        return new RemoveShoppingItemToCartCommand(request.customerId(), request.productId(), request.quantity());
    }

    public static CartRestResponse toResponse(Cart cart) {
        return new CartRestResponse(cart.getCustomerId(), cart.getShoppingItems());
    }
}
