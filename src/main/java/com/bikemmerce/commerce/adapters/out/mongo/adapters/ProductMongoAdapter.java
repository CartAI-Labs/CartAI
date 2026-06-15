package com.bikemmerce.commerce.adapters.out.mongo.adapters;

import com.bikemmerce.commerce.adapters.out.mongo.MongoProductRepository;
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

    private final MongoProductRepository mongoProductRepository;

    @Override
    public void delete(ProductId productId) {
        mongoProductRepository.deleteById(productId.value());
    }

    @Override
    public Product find(ProductId productId) {
        return mongoProductRepository.findById(productId.value()).map(ProductMapper::toProduct).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return mongoProductRepository.findAll().stream().map(ProductMapper::toProduct).toList();
    }

    @Override
    public Product save(Product product) {
        return ProductMapper.toProduct(mongoProductRepository.save(ProductMapper.toProductDocument(product)));
    }
}
