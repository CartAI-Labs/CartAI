/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.common.result.ResultError;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.identity.vos.UserUpdatedEvent;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.identity.UserUpdatedEventPublisherPort;
import lombok.RequiredArgsConstructor;

/**
 * @author Roberto Díaz
 */
@UseCase
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserUpdatedEventPublisherPort userUpdatedEventPublisherPort;

    public Result<User> execute(UpdateUserCommand command) {
        User existingUser = userRepositoryPort.findByUserId(new UserId(command.id()));

        if (existingUser == null) {
            return Result.error(ResultError.NOT_FOUND);
        }

        if (!existingUser.email().value().equals(command.email())) {
            User byEmail = userRepositoryPort.findByEmail(new Email(command.email()));
            if (byEmail != null) {
                return Result.error(ResultError.CONFLICT);
            }
        }

        User updatedUser = new User(
                existingUser.userId(),
                command.name(),
                new Email(command.email()),
                existingUser.passwordHash(),
                command.roles(),
                command.avatarFileId()
        );

        User savedUser = userRepositoryPort.save(updatedUser);

        userUpdatedEventPublisherPort.updated(new UserUpdatedEvent(
                savedUser.userId(),
                savedUser.name(),
                savedUser.email(),
                savedUser.roles()
        ));

        return Result.success(savedUser);
    }
}
