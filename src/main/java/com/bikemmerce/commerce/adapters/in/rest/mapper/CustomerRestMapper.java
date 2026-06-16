package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.customer.CustomerRestResponse;
import com.bikemmerce.commerce.adapters.in.rest.dto.customer.UpdateCustomerRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.product.CreateCustomerRestRequest;
import com.bikemmerce.commerce.application.usecases.commands.CreateCustomerCommand;
import com.bikemmerce.commerce.application.usecases.commands.UpdateCustomerCommand;
import com.bikemmerce.commerce.domain.model.Customer;

public class CustomerRestMapper {

    public static CreateCustomerCommand toCreateCustomerCommand(CreateCustomerRestRequest request) {
        return new CreateCustomerCommand(request.name(), request.email());
    }

    public static UpdateCustomerCommand toUpdateCustomerCommand(UpdateCustomerRestRequest request) {
        return new UpdateCustomerCommand(request.id(), request.name(), request.email());
    }

    public static CustomerRestResponse toResponse(Customer customer) {
        return new CustomerRestResponse(
                customer.getCustomerId().value(), customer.getName(), customer.getEmail().value());
    }
}
