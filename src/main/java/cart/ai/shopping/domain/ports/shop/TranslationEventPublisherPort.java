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
