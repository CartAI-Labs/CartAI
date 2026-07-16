package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.repositories;

import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.ProductTranslationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTranslationMongoRepository extends MongoRepository<ProductTranslationDocument, String> {
    List<ProductTranslationDocument> findByProductId(String productId);

    Optional<ProductTranslationDocument> findByProductIdAndLanguageCode(String productId, String languageCode);
}
