package com.bikemmerce.commerce.domain.model.security;

import com.bikemmerce.commerce.domain.model.security.value.objects.Permission;
import com.bikemmerce.commerce.domain.model.security.value.objects.RoleId;
import lombok.NonNull;

import java.util.Set;

public record Role(@NonNull RoleId id, @NonNull String name, @NonNull Set<Permission> permissions) {

}
