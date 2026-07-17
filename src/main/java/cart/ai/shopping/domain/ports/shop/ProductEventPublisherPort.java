/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.vos.ProductCreatedEvent;
import cart.ai.shopping.domain.model.shop.vos.ProductUpdatedEvent;

/**
 * @author Roberto Díaz
 */
public interface ProductEventPublisherPort {
    void productCreated(ProductCreatedEvent event);

    void productUpdated(ProductUpdatedEvent event);
}
