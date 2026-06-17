package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderPlacedEvent;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.ports.events.OrderPlacedEventPublisherPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@RequiredArgsConstructor
@UseCase
public class CreateOrderUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;
    private final OrderPlacedEventPublisherPort orderPlacedEventPublisherPort;

    public Result<Order> execute(CustomerId customerId) {
        OrderId orderId = new OrderId(
                incrementIdGeneratorPort.increment(Order.class).toString());

        if (orderRepositoryPort.find(orderId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Cart cart = cartRepositoryPort.find(customerId);

        Order order = new Order(
                orderId, cart.getCustomerId(), cart.getShoppingItems(), OrderStatus.CREATED, new Date());


        orderPlacedEventPublisherPort.publish(
                new OrderPlacedEvent(orderId, order.getCustomerId(), order.getTotalPrice(), order.getStatus(), order.getCreateDate()));

        return Result.success(orderRepositoryPort.save(order));
    }
}
