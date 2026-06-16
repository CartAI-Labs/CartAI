package com.bikemmerce.commerce.application.usecases.customer;

import com.bikemmerce.commerce.application.usecases.commands.CreateCustomerCommand;
import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(CreateCustomerCommand command) {
        CustomerId customerId = new CustomerId(UUID.randomUUID().toString());
        Email email = new Email(command.email());

        if (customerRepositoryPort.findByCustomerId(customerId) != null ||
                customerRepositoryPort.findByEmail(email) != null) {

            return Result.error(HttpStatus.CONFLICT.value());
        }

        Customer customer = new Customer(customerId, command.name(), email);

        return Result.success(customerRepositoryPort.save(customer));
    }
}
