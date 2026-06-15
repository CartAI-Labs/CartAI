package com.bikemmerce.commerce.domain.model;

import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@Data
public class Cart {

    @NonNull
    private CustomerId customerId;
    @NonNull
    private List<ShoppingItem> shoppingItems;

    public void addItem(Product product, Integer count) {
        ProductId productId = product.getId();

        Optional<ShoppingItem> existingItem = shoppingItems.stream()
            .filter(item -> productId.equals(item.getProductId()))
            .findFirst();

        if (existingItem.isPresent()) {
            ShoppingItem item = existingItem.get();

            item.setCount(item.getCount() + count);
        } else {
            shoppingItems.add(
                new ShoppingItem(productId, count, product.getPrice()));
        }
    }

    public void removeItem(ProductId productId) {
        shoppingItems.removeIf(
            shoppingItem -> productId.equals(shoppingItem.getProductId()));
    }

}
