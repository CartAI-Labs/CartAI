package cart.ai.infrastructure.out.persistence.mongo.shop;

import cart.ai.infrastructure.out.persistence.mongo.shop.documents.CartDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMongoRepository extends MongoRepository<CartDocument, String> {
}
