/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.identity;

import cart.ai.shopping.domain.model.identity.vos.UserUpdatedEvent;

/**
 * @author Roberto Díaz
 */
public interface UserUpdatedEventPublisherPort {
    void updated(UserUpdatedEvent event);
}
