package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents;

import jakarta.validation.constraints.PositiveOrZero;
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
