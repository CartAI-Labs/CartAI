package com.bikemmerce.commerce.application.usecases.shop.order;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.shop.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetOrderUseCase {

    private final OrderRepositoryPort OrderRepositoryPort;

    public Result<Order> execute(OrderId orderId) {
        Order Order = OrderRepositoryPort.find(orderId);

        if (Order != null) {
            return Result.success(Order);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}