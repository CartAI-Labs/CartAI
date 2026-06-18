package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customers")
@Data
public class CustomerDocument {

    @Id
    private final String id;
    private final String name;
    private final String email;

}
