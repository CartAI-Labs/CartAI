package com.bikemmerce.commerce.application.usecases.order;

import com.bikemmerce.commerce.domain.model.Cart;
import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.constants.OrderStatus;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;
import com.bikemmerce.commerce.domain.ports.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.OrderRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;

    public Result<Order> execute(CustomerId customerId) {
        OrderId orderId = new OrderId(UUID.randomUUID().toString());

        if (orderRepositoryPort.find(orderId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Cart cart = cartRepositoryPort.find(customerId);

        Order order = new Order(
                orderId, cart.getCustomerId(), cart.getShoppingItems(), OrderStatus.CREATED,
                new Date());

        //TOD0 - borrar en otro hilo, vaciar el cart, por ahora lo hago sincrono

        cart.clearItems();

        cartRepositoryPort.save(cart);

        return Result.success(orderRepositoryPort.save(order));
    }
}
