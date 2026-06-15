package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import org.springframework.http.HttpStatus;

public class GetOrderUseCase {

    private OrderRepositoryPort OrderRepositoryPort;

    public Result<Order> execute(OrderId OrderId) {
        Order Order = OrderRepositoryPort.find(OrderId);

        if (Order != null) {
            return Result.success(Order);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}