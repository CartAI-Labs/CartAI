package com.bikemmerce.commerce.adapters.out.mongo.mapper;

import com.bikemmerce.commerce.adapters.out.mongo.documents.dto.ProductDocument;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;

public class ProductMapper {

    public static ProductDocument toProductDocument(Product product) {
        return new ProductDocument(
                product.getId().value(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStock());

    }

    public static Product toProduct(ProductDocument productDocument) {
        return new Product(
                new ProductId(productDocument.getId()), productDocument.getName(),
                productDocument.getDescription(), productDocument.getPrice(), productDocument.getStock());
    }

}
