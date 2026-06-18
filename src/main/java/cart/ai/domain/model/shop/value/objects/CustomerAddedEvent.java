package cart.ai.domain.model.shop.value.objects;

import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.model.security.value.objects.UserId;

public record CustomerAddedEvent(
        UserId userId,
        String name,
        Email email
) {
}