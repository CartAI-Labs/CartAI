package cart.ai.application.usecases.shop.customer;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Customer;
import cart.ai.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(UserId userId) {
        Customer customer = customerRepositoryPort.findByCustomerId(userId);

        if (customer == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        return Result.success(customer);
    }
}