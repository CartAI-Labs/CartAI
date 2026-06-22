/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.identity.events.listeners.handlers;

import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationHandler {

    private final UserRepositoryPort userRepositoryPort;

    public void sendAddUserNotification(UserId userId) {
        User user = userRepositoryPort.findByUserId(userId);
        if (user != null) {
            log.info("email sent to {}", user.email().value());
        }
    }

    public void sendUpdateUserNotification(UserId userId) {
        User user = userRepositoryPort.findByUserId(userId);
        if (user != null) {
            log.info("update email sent to {}", user.email().value());
        }
    }
}
