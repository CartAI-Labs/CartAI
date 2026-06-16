package com.bikemmerce.commerce.application.usecases.product;

import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(String id) {
        ProductId productId = new ProductId(id);

        Product product = productRepositoryPort.find(productId);

        if (product == null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        productRepositoryPort.delete(productId);

        return Result.success(product);
    }
}