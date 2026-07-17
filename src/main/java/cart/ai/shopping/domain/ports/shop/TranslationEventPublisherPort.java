/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.events.TranslationRequestedEvent;

/**
 * Outbound port to publish translation requests to the message broker.
 *
 * @author Roberto Díaz
 */
public interface TranslationEventPublisherPort {
    void publishTranslationRequested(TranslationRequestedEvent event);
}
