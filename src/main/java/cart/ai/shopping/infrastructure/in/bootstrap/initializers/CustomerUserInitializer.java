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
public class CustomerUserInitializer extends BaseUserInitializer {

    @Value("${cartai.bootstrap.user.customer.name:customer}")
    private String customerName;
    @Value("${cartai.bootstrap.user.customer.email:customer@cartai.com}")
    private String customerEmailStr;
    @Value("${cartai.bootstrap.user.customer.password:customer}")
    private String customerPassword;

    public CustomerUserInitializer(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort, CreateUserUseCase createUserUseCase) {
        super(userRepositoryPort, roleRepositoryPort, createUserUseCase);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(3)
    public void initializeCustomer() {
        createUserIfNotFound("CUSTOMER", customerName, customerEmailStr, customerPassword);
    }
}
