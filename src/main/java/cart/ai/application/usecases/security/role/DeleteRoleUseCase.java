package cart.ai.application.usecases.security.role;

import cart.ai.application.annotations.UseCase;
import cart.ai.domain.model.security.Role;
import cart.ai.domain.model.security.value.objects.RoleId;
import cart.ai.domain.ports.security.repositories.RoleRepositoryPort;
import cart.ai.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@UseCase
public class DeleteRoleUseCase {

    private final RoleRepositoryPort roleRepositoryPort;

    public Result<Role> execute(RoleId roleId) {
        Role role = roleRepositoryPort.findByRoleId(roleId);

        if (role == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        roleRepositoryPort.delete(roleId);

        return Result.success(role);
    }
}
