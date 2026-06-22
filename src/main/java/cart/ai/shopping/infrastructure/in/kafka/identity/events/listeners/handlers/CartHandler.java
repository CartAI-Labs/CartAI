/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.identity.events.listeners.handlers;

import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.shop.Cart;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.shop.CartRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class CartHandler {

    private final UserRepositoryPort userRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    public void handleCustomerCart(UserId userId) {
        User user = userRepositoryPort.findByUserId(userId);
        if (user == null || user.roles().stream().noneMatch(r -> r.name().equalsIgnoreCase("CUSTOMER"))) {
            return;
        }

        Cart cart = cartRepositoryPort.find(userId);

        if (cart != null) {
            return;
        }

        cartRepositoryPort.save(new Cart(userId, new ArrayList<>()));
    }
}
