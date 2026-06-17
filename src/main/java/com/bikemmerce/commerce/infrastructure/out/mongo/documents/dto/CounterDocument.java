package com.bikemmerce.commerce.infrastructure.out.mongo.documents.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("increment_counter")
@Data
public class CounterDocument {

    @Id
    private final String id;
    private final Long counter;

}
