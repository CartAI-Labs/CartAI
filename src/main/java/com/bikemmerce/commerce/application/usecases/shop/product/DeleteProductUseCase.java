package com.bikemmerce.commerce.application.usecases.shop.product;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.shop.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
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