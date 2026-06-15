package com.bikemmerce.commerce.adapters.out.mongo;

import com.bikemmerce.commerce.adapters.out.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoProductRepository extends MongoRepository<ProductDocument, String> {}
