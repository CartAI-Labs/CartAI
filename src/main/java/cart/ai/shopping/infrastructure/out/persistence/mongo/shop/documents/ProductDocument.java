/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * @author Roberto Díaz
 */
@Document("products")
@Data
@Builder
public class ProductDocument {

    @Id
    private final String id;

    private final String name;

    private final String description;

    private final BigDecimal price;

    private final Integer stock;

}
