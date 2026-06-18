package com.bikemmerce.commerce.domain.ports.shop.repositories;

import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;

import java.util.List;

public interface ProductRepositoryPort {

    void delete(ProductId productId);

    Product find(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
