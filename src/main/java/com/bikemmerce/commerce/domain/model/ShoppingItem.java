package com.bikemmerce.commerce.domain.model;

import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class ShoppingItem {

    @NonNull
    private ProductId productId;
    @NonNull
    private Integer count;
    @NonNull
    private BigDecimal unitPrice;

}
