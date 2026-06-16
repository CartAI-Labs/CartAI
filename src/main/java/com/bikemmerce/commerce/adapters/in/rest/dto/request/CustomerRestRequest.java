package com.bikemmerce.commerce.adapters.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerRestRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        @NotBlank(message = "Email is mandatory")
        String email
) {
}