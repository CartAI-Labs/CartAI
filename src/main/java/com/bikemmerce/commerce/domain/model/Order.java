package com.bikemmerce.commerce.domain.model;

import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Order {

    @NonNull
    private OrderId orderId;
    @NonNull
    private CustomerId customerId;
    @NonNull
    private List<ShoppingItem> shoppingItems;
    @NonNull
    private OrderStatus status;
    @NonNull
    private Date createDate;

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }

    public void confirm() {
        status = OrderStatus.CONFIRMED;
    }

    public BigDecimal getTotalPrice() {
        return shoppingItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}