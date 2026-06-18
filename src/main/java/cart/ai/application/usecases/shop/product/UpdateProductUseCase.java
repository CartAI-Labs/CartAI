package cart.ai.application.usecases.shop.product;

import cart.ai.application.annotations.UseCase;
import cart.ai.application.usecases.shop.commands.UpdateProductCommand;
import cart.ai.domain.model.shop.Product;
import cart.ai.domain.model.shop.value.objects.ProductId;
import cart.ai.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.domain.result.Result;
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