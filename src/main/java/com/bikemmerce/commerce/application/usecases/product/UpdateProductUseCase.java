package com.bikemmerce.commerce.application.usecases.product;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.commands.UpdateProductCommand;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Result<Product> execute(UpdateProductCommand command) {
        Product product = new Product(new ProductId(command.id()), command.name(), command.description(), command.price(), command.stock());

        if (isUpdatableProduct(product)) {
            return Result.success(productRepositoryPort.save(product));
        }

        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private boolean isUpdatableProduct(Product product) {
        return productRepositoryPort.find(product.getId()) != null;
    }
}