/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop.repositories;

import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public interface OrderRepositoryPort {

    void delete(OrderId orderId);

    Order find(OrderId orderId);

    List<Order> findAll();

    Order save(Order order);

}
