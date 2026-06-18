package com.bikemmerce.commerce.application.usecases.shop.product;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.shop.Product;
import com.bikemmerce.commerce.domain.ports.shop.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ListProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<List<Product>> execute() {
        return Result.success(productRepositoryPort.findAll());
    }

}