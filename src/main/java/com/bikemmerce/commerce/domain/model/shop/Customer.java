package com.bikemmerce.commerce.domain.model.shop;

import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import lombok.NonNull;

public record Customer(@NonNull UserId userId, @NonNull String name, @NonNull Email email) {

}