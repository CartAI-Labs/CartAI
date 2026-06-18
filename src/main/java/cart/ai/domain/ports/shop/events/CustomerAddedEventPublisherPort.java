package cart.ai.domain.ports.shop.events;

import cart.ai.domain.model.shop.value.objects.CustomerAddedEvent;

public interface CustomerAddedEventPublisherPort {
    void added(CustomerAddedEvent event);
}
