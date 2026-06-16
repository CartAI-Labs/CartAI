package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class GetOrderUseCase {

    private final OrderRepositoryPort OrderRepositoryPort;

    public Result<Order> execute(String id) {
        Order Order = OrderRepositoryPort.find(new OrderId(id));

        if (Order != null) {
            return Result.success(Order);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}