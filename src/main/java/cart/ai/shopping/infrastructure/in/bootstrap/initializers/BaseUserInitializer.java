/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.bootstrap.initializers;

import cart.ai.shopping.application.usecases.identity.user.CreateUserUseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.CreateUserCommand;
import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@Slf4j
public abstract class BaseUserInitializer {

    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final CreateUserUseCase createUserUseCase;

    protected void createUserIfNotFound(String roleName, String name, String email, String password) {
        Role role = roleRepositoryPort.findByName(roleName);

        if (role == null) {
            log.error("Cannot create {} user because the role does not exist.", roleName);

            return;
        }

        Email userEmail = new Email(email);

        if (userRepositoryPort.findByEmail(userEmail) == null) {
            log.info("User {} not found. Creating...", email);

            var result = createUserUseCase.execute(new CreateUserCommand(
                    name,
                    email,
                    password,
                    Set.of(role),
                    null
            ));
            if (result.hasError()) {
                log.error("Failed to create {} user: {}", roleName, result.getError());
            } else {
                log.info("{} user created successfully.", roleName);
            }
        } else {
            log.info("{} user already exists.", roleName);
        }
    }
}
