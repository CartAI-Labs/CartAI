/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import java.math.BigDecimal;

/**
 * @author Roberto Díaz
 */
public record ShoppingItemDocument(String id, Integer count, BigDecimal unitPrice) {

}
