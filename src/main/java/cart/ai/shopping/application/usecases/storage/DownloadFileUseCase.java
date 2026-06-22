/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.storage.commands.DownloadFileCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.identity.User;
import cart.ai.shopping.domain.model.identity.vos.UserId;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.identity.UserRepositoryPort;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class DownloadFileUseCase {

    private final StoragePort storagePort;
    private final StoredFileRepositoryPort storedFileRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    public Result<InputStream> execute(DownloadFileCommand command) {
        if (command == null || command.fileName().isBlank()) {
            return Result.error(HttpStatus.BAD_REQUEST.value());
        }

        String fileName = command.fileName();
        StoredFile storedFile = storedFileRepositoryPort.findByFileName(fileName);
        if (storedFile == null) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }

        if (storedFile.ownerId() != null) {
            User requester = userRepositoryPort.findByUserId(new UserId(command.requesterUserId()));
            if (requester == null) {
                return Result.error(HttpStatus.UNAUTHORIZED.value());
            }

            boolean isAdmin = requester.roles().stream()
                    .anyMatch(role -> role.name().equals("ADMIN"));

            if (!storedFile.ownerId().equals(command.requesterUserId()) && !isAdmin) {
                return Result.error(HttpStatus.FORBIDDEN.value());
            }
        }

        try {
            InputStream inputStream = storagePort.downloadFile(fileName);
            return Result.success(inputStream);
        } catch (Exception e) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }
    }
}
