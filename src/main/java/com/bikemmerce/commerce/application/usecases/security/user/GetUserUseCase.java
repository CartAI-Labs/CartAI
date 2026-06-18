package com.bikemmerce.commerce.application.usecases.security.user;

import com.bikemmerce.commerce.application.annotations.UseCase;
import com.bikemmerce.commerce.domain.model.security.User;
import com.bikemmerce.commerce.domain.model.security.value.objects.Email;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.ports.security.UserRepositoryPort;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<User> execute(String id, String email) {
        User user = userRepositoryPort.findByUserId(new UserId(id));

        if (user == null) {
            userRepositoryPort.findByEmail(new Email(email));
        }

        if (user != null) {
            return Result.success(user);
        }

        return Result.error(HttpStatus.NOT_FOUND.value());
    }
}
