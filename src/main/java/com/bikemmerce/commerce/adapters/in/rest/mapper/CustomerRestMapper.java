package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.request.CustomerRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.response.CustomerRestResponse;
import com.bikemmerce.commerce.application.usecases.commands.CreateCustomerCommand;
import com.bikemmerce.commerce.domain.model.Customer;

public class CustomerRestMapper {

    public static CreateCustomerCommand toCreateCustomerCommand(CustomerRestRequest customerRestRequest) {
        return new CreateCustomerCommand(
                customerRestRequest.name(), customerRestRequest.email());
    }

    public static CustomerRestResponse toResponse(Customer customer) {
        return new CustomerRestResponse(
                customer.getCustomerId().value(), customer.getName(), customer.getEmail().value());
    }
}
