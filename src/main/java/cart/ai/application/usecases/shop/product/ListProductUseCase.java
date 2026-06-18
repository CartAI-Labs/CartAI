package cart.ai.application.usecases.shop.product;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.shop.Product;
import cart.ai.domain.ports.shop.repositories.ProductRepositoryPort;
import cart.ai.domain.result.Result;
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