package com.bikemmerce.commerce.application.usecases.shop.customer;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(String id, String email) {
        Customer customer = customerRepositoryPort.findByCustomerId(new UserId(id));

        if (customer == null) {
            customer = customerRepositoryPort.findByEmail(new Email(email));
        }

        if (customer != null) {
            return Result.success(customer);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}