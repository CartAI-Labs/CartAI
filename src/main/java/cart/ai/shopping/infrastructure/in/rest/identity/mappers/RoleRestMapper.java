/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity.mappers;

import cart.ai.shopping.application.usecases.identity.role.commands.CreateRoleCommand;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.CreateRoleRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RoleRestResponse;

/**
 * @author Roberto Díaz
 */
public class RoleRestMapper {

    public static CreateRoleCommand toCreateRoleCommand(CreateRoleRestRequest request) {
        return new CreateRoleCommand(request.name(), request.permissions());
    }

    public static RoleRestResponse toResponse(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleRestResponse(
                role.id().value(),
                role.name(),
                role.permissions().stream().map(Permission::value).toList()
        );
    }
}
