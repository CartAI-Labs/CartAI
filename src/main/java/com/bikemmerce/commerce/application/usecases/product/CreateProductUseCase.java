package com.bikemmerce.commerce.application.usecases.product;

import com.bikemmerce.commerce.application.usecases.commands.CreateProductCommand;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Product> execute(CreateProductCommand command) {
        ProductId productId = new ProductId(
                incrementIdGeneratorPort.increment(Product.class).toString());

        if (productRepositoryPort.find(productId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Product product = new Product(
                productId, command.name(), command.description(), command.price(), command.stock()
        );

        return Result.success(productRepositoryPort.save(product));
    }
}