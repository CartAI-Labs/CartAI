package cart.ai.domain.model.security;

import cart.ai.domain.model.security.value.objects.Permission;
import cart.ai.domain.model.security.value.objects.RoleId;
import lombok.NonNull;

import java.util.Set;

public record Role(@NonNull RoleId id, @NonNull String name, @NonNull Set<Permission> permissions) {

}
