package cart.ai.application.usecases.shop.customer;

import cart.ai.application.annotations.UseCase;
import cart.ai.application.usecases.shop.commands.UpdateCustomerCommand;
import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Customer;
import cart.ai.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase

public class UpdateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(UpdateCustomerCommand command) {
        UserId userId = new UserId(command.id());
        Email email = new Email(command.email());

        if (customerRepositoryPort.findByEmail(email) != null) {
            return Result.error(HttpStatus.CONFLICT.value());
        }

        Customer customer = new Customer(userId, command.name(), email);

        return Result.success(customerRepositoryPort.save(customer));
    }
}
