package com.bikemmerce.commerce.domain.ports;

import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;

import java.util.List;

public interface ProductRepositoryPort {

    void delete(ProductId productId);

    Product find(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
