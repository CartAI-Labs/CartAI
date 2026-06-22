/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.security.services;

import cart.ai.shopping.domain.model.identity.Role;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final JwtService jwtService;

    public String generateToken(User user) {
        Map<String, Object> extraClaims = Map.of(
                "userId", user.userId().value(),
                "roles", user.roles().stream().map(Role::name).toList(),
                "permissions", user.roles().stream()
                        .flatMap(role -> role.permissions().stream())
                        .map(Permission::value)
                        .collect(Collectors.toSet())
        );

        return jwtService.generateToken(user.email().value(), extraClaims);
    }

    public String getUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);

        return jwtService.extractClaim(token, claims -> claims.get("userId", String.class));
    }
}
