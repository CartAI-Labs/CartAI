/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.identity.events.listeners;

import cart.ai.shopping.domain.model.identity.vos.UserUpdatedEvent;
import cart.ai.shopping.infrastructure.in.kafka.identity.events.listeners.handlers.CartHandler;
import cart.ai.shopping.infrastructure.in.kafka.identity.events.listeners.handlers.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class UserUpdatedEventNotificationListener {

    private final NotificationHandler notificationHandler;
    private final CartHandler cartHandler;

    @KafkaListener(topics = "users-topic", groupId = "email-notification")
    public void notify(UserUpdatedEvent event) {
        notificationHandler.sendUpdateUserNotification(event.userId());
    }

    @KafkaListener(topics = "users-topic", groupId = "post-processor")
    public void postProcess(UserUpdatedEvent event) {
        cartHandler.handleCustomerCart(event.userId());
    }
}
