package cart.ai.domain.ports.security.events;

import cart.ai.domain.model.security.value.objects.UserAddedEvent;

public interface UserAddedEventPublisherPort {
    void added(UserAddedEvent event);
}
