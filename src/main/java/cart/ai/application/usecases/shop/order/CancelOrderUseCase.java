package cart.ai.application.usecases.shop.order;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.shop.Order;
import cart.ai.domain.model.shop.value.objects.OrderId;
import cart.ai.domain.ports.shop.repositories.OrderRepositoryPort;
import cart.ai.domain.result.Result;
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