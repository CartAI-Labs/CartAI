package cart.ai.application.usecases.security.user;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.security.User;
import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.ports.security.repositories.UserRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class GetUserByEmailUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<User> execute(Email email) {
        User user = userRepositoryPort.findByEmail(email);

        if (user == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        return Result.success(user);
    }
}
