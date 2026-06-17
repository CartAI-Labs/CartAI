package com.bikemmerce.commerce.application.usecases.customer;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.commands.CreateCustomerCommand;
import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerAddedEvent;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.ports.IncrementIdGeneratorPort;
import com.bikemmerce.commerce.domain.ports.events.CustomerAddedEventPublisherPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CustomerAddedEventPublisherPort customerAddedEventPublisherPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;

    public Result<Customer> execute(CreateCustomerCommand command) {
        CustomerId customerId = new CustomerId(
                incrementIdGeneratorPort.increment(Customer.class).toString());

        Email email = new Email(command.email());

        if (customerRepositoryPort.findByEmail(email) != null) {
            return Result.error(HttpStatus.CONFLICT.value());
        }

        Customer customer = customerRepositoryPort.save(
                new Customer(customerId, command.name(), email));

        customerAddedEventPublisherPort.added(
                new CustomerAddedEvent(customerId, customer.getName(), customer.getEmail()));

        return Result.success(customerRepositoryPort.save(customer));
    }
}
