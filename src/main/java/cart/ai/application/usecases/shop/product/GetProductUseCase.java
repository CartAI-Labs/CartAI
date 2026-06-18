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