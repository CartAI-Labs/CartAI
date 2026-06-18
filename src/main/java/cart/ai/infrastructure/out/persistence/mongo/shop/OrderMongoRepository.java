package cart.ai.infrastructure.out.persistence.mongo.shop;

import cart.ai.infrastructure.out.persistence.mongo.shop.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderDocument, String> {
}
