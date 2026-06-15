package com.bikemmerce.commerce.application.usecases.product;

import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(ProductId productId) {
        Product product = productRepositoryPort.find(productId);

        if (product != null) {
            return Result.success(product);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }
}