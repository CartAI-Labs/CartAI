package com.bikemmerce.commerce.domain.ports.security;

import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;

import java.util.List;

public interface UserRepositoryPort {

    void delete(UserId userId);

    User findByUserId(UserId userId);

    User findByEmail(Email email);

    List<User> findAll();

    User save(User product);
}
