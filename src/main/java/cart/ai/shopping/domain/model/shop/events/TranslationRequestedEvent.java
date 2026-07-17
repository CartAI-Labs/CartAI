/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Roberto Díaz
 */
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
