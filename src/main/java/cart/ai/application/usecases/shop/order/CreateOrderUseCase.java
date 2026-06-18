package cart.ai.application.usecases.shop.order;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.constants.OrderStatus;
import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Cart;
import cart.ai.domain.model.shop.Order;
import cart.ai.domain.model.shop.value.objects.OrderId;
import cart.ai.domain.model.shop.value.objects.OrderPlacedEvent;
import cart.ai.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.domain.ports.shop.events.OrderPlacedEventPublisherPort;
import cart.ai.domain.ports.shop.repositories.CartRepositoryPort;
import cart.ai.domain.ports.shop.repositories.OrderRepositoryPort;
import cart.ai.domain.result.Result;
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

    public Result<Order> execute(UserId userId) {
        OrderId orderId = new OrderId(incrementIdGeneratorPort.generate(Order.class));

        if (orderRepositoryPort.find(orderId) != null) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        Cart cart = cartRepositoryPort.find(userId);

        Order order = orderRepositoryPort.save(new Order(
                orderId,
                cart.getUserId(),
                cart.getShoppingItems(),
                OrderStatus.CREATED,
                new Date()));

        orderPlacedEventPublisherPort.publish(
                new OrderPlacedEvent(
                        orderId, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getCreateDate()));

        return Result.success(order);
    }
}
