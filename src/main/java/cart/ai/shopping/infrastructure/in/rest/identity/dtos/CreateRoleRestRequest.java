/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public record CreateRoleRestRequest(
        @NotBlank String name,
        @NotEmpty Set<String> permissions
) {
}
