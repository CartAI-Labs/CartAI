package com.bikemmerce.commerce.application.usecases.customer;

import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.ports.CustomerRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;

    public Result<Customer> execute(Customer customer) {
        if (customerRepositoryPort.findByCustomerId(customer.getCustomerId()) != null ||
                customerRepositoryPort.findByEmail(customer.getEmail()) != null) {

            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return Result.success(customerRepositoryPort.save(customer));
    }
}
