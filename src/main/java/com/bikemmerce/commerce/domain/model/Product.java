package com.bikemmerce.commerce.domain.model;

import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class Product {

    @NonNull
    private ProductId id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Integer stock;

    public void increaseStock(Integer count) {
        this.stock += count;
    }

    public void decreaseStock(Integer count) {
        this.stock -= count;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }

}