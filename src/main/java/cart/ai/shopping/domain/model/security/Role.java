/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.security;

import cart.ai.shopping.domain.model.security.value.objects.Permission;
import cart.ai.shopping.domain.model.security.value.objects.RoleId;
import lombok.NonNull;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record Role(@NonNull RoleId id, @NonNull String name, @NonNull Set<Permission> permissions) {

}
