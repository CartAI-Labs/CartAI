package com.bikemmerce.commerce.application.usecases.shop.commands;

import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RemoveShoppingItemToCartCommand(
        @NotNull(message = "CustomerId is mandatory")
        UserId userId,

        @NotNull(message = "ProductId is mandatory")
        ProductId productId,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock could not be negative")
        Integer quantity
) {
}
