package cart.ai.application.usecases.security.role;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.security.Role;
import cart.ai.domain.ports.security.repositories.RoleRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class ListRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public Result<List<Role>> execute() {
        return Result.success(roleRepositoryPort.findAll());
    }
}
