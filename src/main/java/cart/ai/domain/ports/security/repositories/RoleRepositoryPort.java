package cart.ai.domain.ports.security.repositories;

import cart.ai.domain.model.security.Role;
import cart.ai.domain.model.security.value.objects.RoleId;
import cart.ai.domain.model.security.value.objects.UserId;

import java.util.List;

public interface RoleRepositoryPort {

    void delete(RoleId userId);

    Role findByRoleId(RoleId userId);

    Role findByName(String name);

    List<Role> findUserRoles(UserId userId);

    List<Role> findAll();

    Role save(Role product);
}
