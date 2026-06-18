package com.bikemmerce.commerce.application.usecases.shop.customer;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.application.usecases.shop.commands.UpdateCustomerCommand;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
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
