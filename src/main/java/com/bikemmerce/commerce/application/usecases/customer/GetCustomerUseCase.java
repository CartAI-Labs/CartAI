package com.bikemmerce.commerce.application.usecases.customer;

import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class GetCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(String id, String email) {
        Customer customer = customerRepositoryPort.findByCustomerId(new CustomerId(id));

        if (customer == null) {
            customer = customerRepositoryPort.findByEmail(new Email(email));
        }

        if (customer != null) {
            return Result.success(customer);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}