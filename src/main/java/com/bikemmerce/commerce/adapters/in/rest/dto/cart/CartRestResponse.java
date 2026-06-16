package com.bikemmerce.commerce.adapters.in.rest.dto.cart;

import com.bikemmerce.commerce.domain.model.ShoppingItem;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;

import java.util.List;

public record CartRestResponse(
        CustomerId customerId,
        List<ShoppingItem> shoppingItems
) {
}
