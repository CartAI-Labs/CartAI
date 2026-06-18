package cart.ai.application.usecases.shop.customer;

import cart.ai.application.annotations.UseCase;
import cart.ai.application.usecases.shop.commands.CreateCustomerCommand;
import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Customer;
import cart.ai.domain.model.shop.value.objects.CustomerAddedEvent;
import cart.ai.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.domain.ports.shop.events.CustomerAddedEventPublisherPort;
import cart.ai.domain.ports.shop.repositories.CustomerRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerAddedEventPublisherPort customerAddedEventPublisherPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Customer> execute(CreateCustomerCommand command) {
        UserId userId = new UserId(incrementIdGeneratorPort.generate(Customer.class));

        Email email = new Email(command.email());

        if (customerRepositoryPort.findByEmail(email) != null) {
            return Result.error(HttpStatus.CONFLICT.value());
        }

        Customer customer = customerRepositoryPort.save(
                new Customer(userId, command.name(), email));

        customerAddedEventPublisherPort.added(
                new CustomerAddedEvent(userId, customer.name(), customer.email()));

        return Result.success(customerRepositoryPort.save(customer));
    }
}
