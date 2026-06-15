package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import org.springframework.http.HttpStatus;

public class CreateOrderUseCase {

    private OrderRepositoryPort orderRepositoryPort;

    public Result<Order> execute(Order order) {
        if (orderRepositoryPort.find(order.getOrderId()) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return Result.success(orderRepositoryPort.save(order));
    }
}
