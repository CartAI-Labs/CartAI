/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Roberto Díaz
 */
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
