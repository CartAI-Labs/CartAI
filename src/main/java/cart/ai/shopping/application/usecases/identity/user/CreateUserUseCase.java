/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.CreateUserCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.Email;
import cart.ai.shopping.domain.model.identity.vos.UserAddedEvent;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.identity.PasswordEncoderPort;
import cart.ai.shopping.domain.ports.identity.UserAddedEventPublisherPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static cart.ai.shopping.domain.common.result.ResultError.CONFLICT;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserAddedEventPublisherPort userAddedEventPublisherPort;
    private final IncrementIdGeneratorPort incrementIdGeneratorPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final StoragePort storagePort;
    private final TempStoragePort tempStoragePort;

    public Result<User> execute(CreateUserCommand command) {
        Email email = new Email(command.email());

        if (userRepositoryPort.findByEmail(email) != null) {
            return Result.error(CONFLICT);
        }

        UserId userId = new UserId(incrementIdGeneratorPort.generate(User.class));
        String passwordHash = passwordEncoderPort.encode(command.password());
        String finalAvatarId = null;
        if (command.avatarFileId() != null && !command.avatarFileId().isBlank()) {
            try {
                storagePort.promoteFile(command.avatarFileId(), tempStoragePort.getBucketName());
                finalAvatarId = command.avatarFileId();
            } catch (Exception e) {
                log.warn("Could not promote temp avatar {}: {}", command.avatarFileId(), e.getMessage());
            }
        }

        User user = userRepositoryPort.save(new User(
                userId,
                command.name(),
                email,
                passwordHash,
                command.roles(),
                finalAvatarId
        ));

        userAddedEventPublisherPort.added(new UserAddedEvent(
                user.userId(),
                user.name(),
                user.email(),
                user.roles()
        ));

        return Result.success(user);
    }
}
