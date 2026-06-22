/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.storage.commands.UploadFileCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class UploadFileUseCase {

    private final StoragePort storagePort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;
    private final IncrementIdGeneratorPort idGeneratorPort;
    private final UserRepositoryPort userRepositoryPort;

    private static @NonNull String getExtension(UploadFileCommand command) {
        String originalFileName = command.originalFileName();
        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }
        return extension;
    }

    public Result<StoredFile> execute(UploadFileCommand command) {
        if (command == null || command.inputStream() == null || command.originalFileName().isBlank()) {
            return Result.error(HttpStatus.BAD_REQUEST.value());
        }

        User requester = userRepositoryPort.findByUserId(new UserId(command.requesterUserId()));
        if (requester == null) {
            return Result.error(HttpStatus.UNAUTHORIZED.value());
        }

        boolean isAdmin = requester.roles().stream()
                .anyMatch(role -> role.name().equals("ADMIN"));

        if (command.ownerId() != null) {
            if (!command.ownerId().equals(command.requesterUserId()) && !isAdmin) {
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

        String id = idGeneratorPort.generate(StoredFile.class);

        String uniqueFileName = id + getExtension(command);

        try {
            StoredFile result = storagePort.uploadFile(command.inputStream(), , command.contentType(), command.contentLength());
            StoredFile storedFile = new StoredFile(
                    id,
                    result.fileName(),
                    result.fileUrl(),
                    result.contentType(),
                    command.ownerId()
            );

            StoredFile saved = storedFileRepositoryPort.save(storedFile);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
