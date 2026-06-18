package cart.ai.application.usecases.shop.product;

import cart.ai.application.annotations.UseCase;
import cart.ai.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.domain.model.shop.Product;
import cart.ai.domain.model.shop.value.objects.ProductId;
import cart.ai.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Product> execute(CreateProductCommand command) {
        ProductId productId = new ProductId(incrementIdGeneratorPort.generate(Product.class));

        if (productRepositoryPort.find(productId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Product product = new Product(
                productId,
                command.name(),
                command.description(),
                command.price(),
                command.stock()
        );

        return Result.success(productRepositoryPort.save(product));
    }
}