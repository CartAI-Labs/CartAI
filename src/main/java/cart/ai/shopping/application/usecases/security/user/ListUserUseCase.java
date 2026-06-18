/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.security.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.model.security.User;
import cart.ai.shopping.domain.ports.security.repositories.UserRepositoryPort;
import cart.ai.shopping.domain.result.Result;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class ListUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public Result<List<User>> execute() {
        return Result.success(userRepositoryPort.findAll());
    }
}
