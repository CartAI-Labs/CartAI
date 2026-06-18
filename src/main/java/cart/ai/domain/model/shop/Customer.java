package cart.ai.domain.model.shop;

import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.model.security.value.objects.UserId;
import lombok.NonNull;

public record Customer(@NonNull UserId userId, @NonNull String name, @NonNull Email email) {

}