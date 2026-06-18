package com.bikemmerce.commerce.application.usecases.shop.order;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.shop.repositories.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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