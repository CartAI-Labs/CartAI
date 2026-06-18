/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@Document("cart")
@Data
@Builder
public class CartDocument {

    @Id
    private final String customerId;

    private final List<ShoppingItemDocument> shoppingItems;

}
