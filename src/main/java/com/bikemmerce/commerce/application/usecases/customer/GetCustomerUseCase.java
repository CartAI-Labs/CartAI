package com.bikemmerce.commerce.application.usecases.customer;

import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import org.springframework.http.HttpStatus;

public class GetCustomerUseCase {

    private CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(CustomerId customerId, Email email) {
        Customer customer = customerRepositoryPort.findByCustomerId(customerId);

        if (customer == null) {
            customer = customerRepositoryPort.findByEmail(email);
        }

        if (customer != null) {
            return Result.success(customer);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }

}