package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.mapper;

import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents.CartDocument;

public class CartMapper {

    public static CartDocument toDocument(Cart cart) {
        return new CartDocument(
                cart.getUserId().value(), cart.getShoppingItems().stream().map(ShoppingItemMapper::toDocument).toList());
    }

    public static Cart toDomain(CartDocument cartDocument) {
        return new Cart(
                new UserId(cartDocument.getCustomerId()),
                cartDocument.getShoppingItems().stream().map(ShoppingItemMapper::toDomain).toList());
    }

}
