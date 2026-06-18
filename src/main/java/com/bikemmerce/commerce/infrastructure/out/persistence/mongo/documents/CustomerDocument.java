package com.bikemmerce.commerce.infrastructure.out.persistence.mongo.documents;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
