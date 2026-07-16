package cart.ai.shopping.domain.model.shop.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Event received when a translation is completed.
 *
 * @author Roberto Díaz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationCompletedEvent {
    private String productId;
    private Map<String, ProductTranslationData> translations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductTranslationData {
        private String name;
        private String description;
        private Map<String, String> attributes;
    }
}
