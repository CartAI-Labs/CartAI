package com.bikemmerce.commerce.application.usecases.shop.product;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.model.shop.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.shop.repositories.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(String id) {
        Product product = productRepositoryPort.find(new ProductId(id));

        if (product != null) {
            return Result.success(product);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }
}