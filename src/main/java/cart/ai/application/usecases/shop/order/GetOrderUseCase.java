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