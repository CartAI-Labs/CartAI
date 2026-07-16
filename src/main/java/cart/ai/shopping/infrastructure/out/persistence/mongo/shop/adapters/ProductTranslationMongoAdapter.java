package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.adapters;

import cart.ai.shopping.domain.model.shop.ProductTranslation;
import cart.ai.shopping.domain.ports.shop.ProductTranslationRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.ProductTranslationDocument;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers.ProductTranslationMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.repositories.ProductTranslationMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductTranslationMongoAdapter implements ProductTranslationRepositoryPort {

    private final MongoTemplate mongoTemplate;
    private final ProductTranslationMongoRepository repository;
    private final ProductTranslationMapper mapper;

    @Override
    public ProductTranslation save(ProductTranslation translation) {
        Query query = Query.query(Criteria.where("product_id").is(translation.getProductId())
                .and("language_code").is(translation.getLanguageCode()));

        Update update = new Update()
                .set("name", translation.getName())
                .set("description", translation.getDescription())
                .set("attributes", translation.getAttributes());

        mongoTemplate.upsert(query, update, ProductTranslationDocument.class);

        return translation;
    }

    @Override
    public List<ProductTranslation> saveAll(List<ProductTranslation> translations) {
        return translations.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public List<ProductTranslation> findByProductId(String productId) {
        return mapper.toDomainList(repository.findByProductId(productId));
    }

    @Override
    public Optional<ProductTranslation> findByProductIdAndLanguageCode(String productId, String languageCode) {
        return repository.findByProductIdAndLanguageCode(productId, languageCode)
                .map(mapper::toDomain);
    }
}
