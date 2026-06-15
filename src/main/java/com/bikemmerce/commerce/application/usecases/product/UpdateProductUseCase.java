package com.bikemmerce.commerce.application.usecases.product;

import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(Product product) {
        if (isUpdatableProduct(product)) {
            return Result.success(productRepositoryPort.save(product));
        }

        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private boolean isUpdatableProduct(Product product) {
        return productRepositoryPort.find(product.getId()) != null;
    }
}