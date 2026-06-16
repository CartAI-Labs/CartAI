package com.bikemmerce.commerce.adapters.in.rest.dto.product;

import com.bikemmerce.commerce.domain.model.value.objects.ProductId;

import java.math.BigDecimal;

public record ProductRestResponse(
        ProductId id,
        String name,
        String description,
        BigDecimal price,
        Integer stock
) {
}
