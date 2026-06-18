package cart.ai.domain.ports.shop.events;

import cart.ai.domain.model.shop.value.objects.OrderPlacedEvent;

public interface OrderPlacedEventPublisherPort {
    void publish(OrderPlacedEvent event);
}
