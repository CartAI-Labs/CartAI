/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.bootstrap.initializers;

import cart.ai.shopping.application.usecases.identity.user.CreateUserUseCase;
import cart.ai.shopping.domain.ports.identity.RoleRepositoryPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Roberto Díaz
 */
@Component
@ConditionalOnProperty(prefix = "cartai.bootstrap", name = "enabled", havingValue = "true")
public class AdminUserInitializer extends BaseUserInitializer {

    @Value("${cartai.bootstrap.user.admin.name:cartai}")
    private String adminName;
    @Value("${cartai.bootstrap.user.admin.email:cartai@cartai.com}")
    private String adminEmailStr;
    @Value("${cartai.bootstrap.user.admin.password:cartai}")
    private String adminPassword;

    public AdminUserInitializer(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort, CreateUserUseCase createUserUseCase) {
        super(userRepositoryPort, roleRepositoryPort, createUserUseCase);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    public void initializeAdmin() {
        createUserIfNotFound("ADMIN", adminName, adminEmailStr, adminPassword);
    }
}
