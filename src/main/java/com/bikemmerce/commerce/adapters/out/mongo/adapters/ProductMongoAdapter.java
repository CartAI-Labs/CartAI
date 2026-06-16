package com.bikemmerce.commerce.adapters.out.mongo.adapters;

import com.bikemmerce.commerce.adapters.out.mongo.ProductMongoRepository;
import com.bikemmerce.commerce.adapters.out.mongo.mapper.ProductMapper;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.ports.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMongoAdapter implements ProductRepositoryPort {

    private final ProductMongoRepository productMongoRepository;

    @Override
    public void delete(ProductId productId) {
        productMongoRepository.deleteById(productId.value());
    }

    @Override
    public Product find(ProductId productId) {
        return productMongoRepository.findById(productId.value()).map(ProductMapper::toProduct).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return productMongoRepository.findAll().stream().map(ProductMapper::toProduct).toList();
    }

    @Override
    public Product save(Product product) {
        return ProductMapper.toProduct(productMongoRepository.save(ProductMapper.toProductDocument(product)));
    }
}
