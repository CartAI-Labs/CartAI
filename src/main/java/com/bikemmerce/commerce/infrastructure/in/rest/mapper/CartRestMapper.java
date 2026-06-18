package com.bikemmerce.commerce.infrastructure.in.rest.mapper;

import com.bikemmerce.commerce.application.usecases.shop.commands.AddShoppingItemToCartCommand;
import com.bikemmerce.commerce.application.usecases.shop.commands.RemoveShoppingItemToCartCommand;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.AddShoppingItemToCartRestRequest;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.CartRestResponse;
import com.bikemmerce.commerce.infrastructure.in.rest.dto.cart.RemoveShoppingItemToCartRestRequest;

public class CartRestMapper {

    public static AddShoppingItemToCartCommand toAddShoppingItemToCartCommand(AddShoppingItemToCartRestRequest request) {
        return new AddShoppingItemToCartCommand(
                new UserId(request.customerId()), new ProductId(request.productId()), request.quantity());
    }


    public static RemoveShoppingItemToCartCommand toRemoveShoppingItemToCartCommand(RemoveShoppingItemToCartRestRequest request) {
        return new RemoveShoppingItemToCartCommand(
                new UserId(request.customerId()), new ProductId(request.productId()), request.quantity());
    }

    public static CartRestResponse toResponse(Cart cart) {
        return new CartRestResponse(cart.getUserId().value(), cart.getShoppingItems());
    }
}
