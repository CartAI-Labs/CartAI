/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.UpdateUserAvatarCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;
import static cart.ai.shopping.domain.common.result.ResultError.NOT_FOUND;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class UpdateUserAvatarUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final StoragePort storagePort;

    public Result<User> execute(UpdateUserAvatarCommand command) {
        User user = userRepositoryPort.findByUserId(new UserId(command.userId()));
        if (user == null) {
            return Result.error(NOT_FOUND);
        }

        String oldAvatarId = user.avatarFileId();

        try {
            StoredFile newAvatar = storagePort.uploadFile(
                    command.inputStream(),
                    command.fileName(),
                    command.contentType(),
                    command.contentLength()
            );

            if (oldAvatarId != null && !oldAvatarId.isBlank()) {
                try {
                    storagePort.deleteFile(oldAvatarId);
                } catch (Exception e) {
                    log.warn("Could not delete old avatar ({}): {}", oldAvatarId, e.getMessage());
                }
            }

            User updatedUser = new User(
                    user.userId(),
                    user.name(),
                    user.email(),
                    user.passwordHash(),
                    user.roles(),
                    newAvatar.fileName()
            );

            return Result.success(userRepositoryPort.save(updatedUser));
        } catch (Exception e) {
            log.error("Error updating avatar for user {}: {}", command.userId(), e.getMessage(), e);
            return Result.error(INTERNAL_ERROR);
        }
    }
}
