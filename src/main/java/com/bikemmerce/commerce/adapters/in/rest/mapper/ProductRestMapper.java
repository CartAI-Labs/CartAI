package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.request.ProductRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.response.ProductRestResponse;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;

import java.util.UUID;

public class ProductRestMapper {

    public static Product toDomain(ProductRestRequest productRestRequest) {
        ProductId productId = new ProductId(UUID.randomUUID().toString());

        return new Product(
            productId, productRestRequest.name(), productRestRequest.description(),
                productRestRequest.price(),productRestRequest.stock());
    }

    public static ProductRestResponse toResponse(Product product) {
        return new ProductRestResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock());
    }
}
