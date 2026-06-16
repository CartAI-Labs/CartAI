package com.bikemmerce.commerce.adapters.in.rest;

import com.bikemmerce.commerce.adapters.in.rest.dto.request.CustomerRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.mapper.CustomerRestMapper;
import com.bikemmerce.commerce.application.usecases.customer.CreateCustomerUseCase;
import com.bikemmerce.commerce.application.usecases.customer.GetCustomerUseCase;
import com.bikemmerce.commerce.domain.model.Customer;
import com.bikemmerce.commerce.domain.model.value.objects.CustomerId;
import com.bikemmerce.commerce.domain.model.value.objects.Email;
import com.bikemmerce.commerce.domain.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody @Valid CustomerRestRequest customerRestRequest) {
        Result<Customer> result = createCustomerUseCase.execute(CustomerRestMapper.toCreateCustomerCommand(customerRestRequest));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        Result<Customer> result = getCustomerUseCase.execute(id, null);

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {
        Result<Customer> result = getCustomerUseCase.execute(null, email);

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(CustomerRestMapper.toResponse(result.getValue()));
    }

}
