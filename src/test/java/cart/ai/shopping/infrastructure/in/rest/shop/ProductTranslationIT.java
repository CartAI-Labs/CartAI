package cart.ai.shopping.infrastructure.in.rest.shop;

import cart.ai.shopping.infrastructure.in.rest.BaseIT;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.ProductTranslationDocument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductTranslationIT extends BaseIT {

    private static final String COLLECTION_PRODUCT_TRANSLATIONS = "product_translations";
    private static final String COLLECTION_OUTBOX = "outbox_transaction";

    @AfterEach
    void cleanup() {
        cleanCollections(COLLECTION_PRODUCTS, COLLECTION_PRODUCT_TRANSLATIONS, COLLECTION_OUTBOX);
    }

    private String createProductAsVendor(String name) throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);
        CreateProductRestRequest req = new CreateProductRestRequest(
                name, "Test description", new BigDecimal("99.99"), 10, List.of(), Map.of()
        );
        String response = mockMvc.perform(post("/api/products")
                        .header("Authorization", bearerOf(auth.get("token").asText()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asText();
    }

    @Test
    void requestTranslationForExistingProductReturns200AndSavesOutboxEvent() throws Exception {
        String productId = createProductAsVendor("Translatable Product");
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);

        mockMvc.perform(post("/api/products/" + productId + "/translations")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isOk());

        long outboxCount = mongoTemplate.count(
                Query.query(Criteria.where("aggregateId").is(productId)
                        .and("topic").is("translation.requested")),
                COLLECTION_OUTBOX);

        assertEquals(1, outboxCount);
    }

    @Test
    void requestTranslationForNonExistentProductReturns404() throws Exception {
        var auth = login(VENDOR_EMAIL, VENDOR_PASS);

        mockMvc.perform(post("/api/products/non-existent-id/translations")
                        .header("Authorization", bearerOf(auth.get("token").asText())))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductByIdWithLangReturnsMatchingTranslation() throws Exception {
        String productId = createProductAsVendor("Product With Translation");

        mongoTemplate.save(ProductTranslationDocument.builder()
                .productId(productId)
                .languageCode("es_ES")
                .name("Producto traducido")
                .description("Descripción traducida")
                .attributes(Map.of())
                .build());

        mockMvc.perform(get("/api/products/" + productId).param("lang", "es_ES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Producto traducido"))
                .andExpect(jsonPath("$.description").value("Descripción traducida"));
    }

    @Test
    void getProductByIdWithMissingLangFallsBackToDefaultLanguage() throws Exception {
        String productId = createProductAsVendor("Product With Default Translation");

        mongoTemplate.save(ProductTranslationDocument.builder()
                .productId(productId)
                .languageCode("en_US")
                .name("Translated Product")
                .description("Translated description")
                .attributes(Map.of())
                .build());

        mockMvc.perform(get("/api/products/" + productId).param("lang", "fr_FR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Translated Product"));
    }

    @Test
    void getProductByIdWithLangAndNoTranslationReturnsBaseProduct() throws Exception {
        String productId = createProductAsVendor("Untranslated Product");

        mockMvc.perform(get("/api/products/" + productId).param("lang", "es_ES"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Untranslated Product"));
    }
}
