package com.bikemmerce.commerce.domain.model.security.value.objects;

import com.bikemmerce.commerce.domain.model.security.Role;
import lombok.NonNull;

import java.util.Set;

public record UserAddedEvent(
        @NonNull UserId userId,
        @NonNull String name,
        @NonNull Email email,
        @NonNull Set<Role> roles
) {
}
