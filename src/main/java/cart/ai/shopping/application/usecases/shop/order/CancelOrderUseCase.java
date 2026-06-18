/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.order;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.shop.Order;
import cart.ai.shopping.domain.model.shop.value.objects.OrderId;
import cart.ai.shopping.domain.ports.shop.repositories.OrderRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public Result<Order> execute(OrderId orderId) {
        Order order = orderRepositoryPort.find(orderId);

        if (order != null) {
            order.cancel();

            return Result.success(orderRepositoryPort.save(order));
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}
