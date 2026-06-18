package com.bikemmerce.commerce.domain.ports.security.repositories;

import com.bikemmerce.commerce.domain.model.security.Role;
import com.bikemmerce.commerce.domain.model.security.value.objects.RoleId;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;

import java.util.List;

public interface RoleRepositoryPort {

    void delete(RoleId userId);

    Role findByRoleId(RoleId userId);

    List<Role> findUserRoles(UserId userId);

    List<Role> findAll();

    Role save(Role product);
}
