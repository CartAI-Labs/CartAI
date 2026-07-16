package cart.ai.shopping.domain.model.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Entity representing a translated product in a specific language.
 *
 * @author Roberto Díaz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTranslation {
    private String productId;
    private String languageCode;
    private String name;
    private String description;
    private Map<String, String> attributes;
}
