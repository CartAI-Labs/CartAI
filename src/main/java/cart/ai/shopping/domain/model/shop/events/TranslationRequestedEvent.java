package cart.ai.shopping.domain.model.shop.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Event emitted to request a translation of a product.
 *
 * @author Roberto Díaz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranslationRequestedEvent {
    private String productId;
    private String name;
    private String description;
    private Map<String, String> attributes;
    private List<String> targetLanguages;
}
