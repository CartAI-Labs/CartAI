/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.bootstrap.initializers;

import cart.ai.shopping.application.usecases.identity.role.CreateRoleUseCase;
import cart.ai.shopping.application.usecases.identity.role.commands.CreateRoleCommand;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import cart.ai.shopping.infrastructure.in.bootstrap.properties.BootstrapProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "cartai.bootstrap", name = "enabled", havingValue = "true")
public class RoleInitializer {

    private final RoleRepositoryPort roleRepositoryPort;
    private final CreateRoleUseCase createRoleUseCase;
    private final BootstrapProperties bootstrapProperties;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void initializeRoles() {
        if (bootstrapProperties.getRoles() != null) {
            bootstrapProperties.getRoles().forEach(this::createRoleIfNotFound);
        }
    }

    private void createRoleIfNotFound(String roleName, Set<String> permissions) {
        if (roleRepositoryPort.findByName(roleName) == null) {
            log.info("{} role not found. Creating...", roleName);
            var result = createRoleUseCase.execute(new CreateRoleCommand(roleName, permissions));
            if (result.hasError()) {
                log.error("Failed to create {} role: {}", roleName, result.getError());
            } else {
                log.info("{} role created successfully.", roleName);
            }
        }
    }
}
