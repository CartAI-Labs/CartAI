package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_translations")
@CompoundIndex(name = "product_lang_unique", def = "{'product_id': 1, 'language_code': 1}", unique = true)
public class ProductTranslationDocument {

    @Id
    private String id;

    @Field("product_id")
    private String productId;

    @Field("language_code")
    private String languageCode;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("attributes")
    private Map<String, String> attributes;
}
