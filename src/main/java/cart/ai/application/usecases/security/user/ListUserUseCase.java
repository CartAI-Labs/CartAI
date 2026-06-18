package cart.ai.application.usecases.security.user;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.security.User;
import cart.ai.domain.ports.security.repositories.UserRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ListUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<List<User>> execute() {
        return Result.success(userRepositoryPort.findAll());
    }
}
