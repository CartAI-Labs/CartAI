package cart.ai.infrastructure.in.rest.mapper;

import cart.ai.application.usecases.shop.commands.CreateCustomerCommand;
import cart.ai.application.usecases.shop.commands.UpdateCustomerCommand;
import cart.ai.domain.model.shop.Customer;
import cart.ai.infrastructure.in.rest.dto.customer.CreateCustomerRestRequest;
import cart.ai.infrastructure.in.rest.dto.customer.CustomerRestResponse;
import cart.ai.infrastructure.in.rest.dto.customer.UpdateCustomerRestRequest;

public class CustomerRestMapper {

    public static CreateCustomerCommand toCreateCustomerCommand(CreateCustomerRestRequest request) {
        return new CreateCustomerCommand(request.name(), request.email());
    }

    public static UpdateCustomerCommand toUpdateCustomerCommand(UpdateCustomerRestRequest request) {
        return new UpdateCustomerCommand(request.id(), request.name(), request.email());
    }

    public static CustomerRestResponse toResponse(Customer customer) {
        return new CustomerRestResponse(
                customer.userId().value(), customer.name(), customer.email().value());
    }
}
