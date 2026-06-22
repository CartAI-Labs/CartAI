/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.storage.commands.DeleteFileCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.model.storage.vos.StoredFileEvent;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoredFileEventPublisherPort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class DeleteFileUseCase {

    private final StoredFileEventPublisherPort storedFileEventPublisherPort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Transactional
    public Result<Void> execute(DeleteFileCommand command) {
        if (command == null || command.id().isBlank()) {
            return Result.error(HttpStatus.BAD_REQUEST.value());
        }

        User requester = userRepositoryPort.findByUserId(new UserId(command.requesterUserId()));
        if (requester == null) {
            return Result.error(HttpStatus.UNAUTHORIZED.value());
        }

        String id = command.id();
        StoredFile storedFile = storedFileRepositoryPort.findById(id);
        if (storedFile == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        boolean isAdmin = requester.roles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));

        if (storedFile.ownerId() != null) {
            if (!storedFile.ownerId().equals(command.requesterUserId()) && !isAdmin) {
                return Result.error(HttpStatus.FORBIDDEN.value());
            }
        } else {
            boolean hasWritePermission = requester.roles().stream()
                    .flatMap(role -> role.permissions().stream())
                    .anyMatch(permission -> permission.value().equals("WRITE_PRODUCTS"));
            if (!hasWritePermission && !isAdmin) {
                return Result.error(HttpStatus.FORBIDDEN.value());
            }
        }

        try {
            storedFileRepositoryPort.deleteById(id);
            storedFileEventPublisherPort.deletionConfirmed(new StoredFileEvent(storedFile.fileName()));
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
