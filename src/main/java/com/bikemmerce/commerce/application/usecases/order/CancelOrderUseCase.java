package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import org.springframework.http.HttpStatus;

public class CancelOrderUseCase {

    private OrderRepositoryPort orderRepositoryPort;

    public Result<Order> execute(OrderId orderId) {
        Order order = orderRepositoryPort.find(orderId);

        if (order != null) {
            orderRepositoryPort.delete(orderId);

            return Result.success(order);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}