/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Roberto Díaz
 */
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
