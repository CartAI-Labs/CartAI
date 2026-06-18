package cart.ai.domain.ports.shop.repositories;

import cart.ai.domain.model.shop.Product;
import cart.ai.domain.model.shop.value.objects.ProductId;

import java.util.List;

public interface ProductRepositoryPort {

    void delete(ProductId productId);

    Product find(ProductId productId);

    List<Product> findAll();

    Product save(Product product);
}
