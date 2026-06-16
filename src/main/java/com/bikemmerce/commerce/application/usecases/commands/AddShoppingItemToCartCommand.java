package com.bikemmerce.commerce.application.usecases.commands;

import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddShoppingItemToCartCommand(
        @NotNull(message = "CustomerId is mandatory")
        CustomerId customerId,

        @NotNull(message = "ProductId is mandatory")
        ProductId productId,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock could not be negative")
        Integer quantity
) {
}
