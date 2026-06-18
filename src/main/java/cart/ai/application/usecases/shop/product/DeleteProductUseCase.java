package cart.ai.application.usecases.shop.product;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.shop.Product;
import cart.ai.domain.model.shop.value.objects.ProductId;
import cart.ai.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.domain.result.Result;
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