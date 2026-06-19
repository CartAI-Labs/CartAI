/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import cart.ai.shopping.domain.model.shop.constants.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author Roberto Díaz
 */
@Document("order")
@Data
@Builder
public class OrderDocument {

    @Id
    private final String orderId;

    private final String customerId;

    private final List<ShoppingItemDocument> shoppingItems;

    private final OrderStatus orderStatus;

    private final Date createDate;

}
