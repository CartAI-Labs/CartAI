package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public Result<Order> execute(String id) {
        OrderId orderId = new OrderId(id);

        Order order = orderRepositoryPort.find(orderId);

        if (order != null) {
            orderRepositoryPort.delete(orderId);

            return Result.success(order);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}