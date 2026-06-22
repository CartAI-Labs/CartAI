/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.application.usecases.identity.user.commands.UploadAvatarCommand;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class UploadAvatarUseCase {

    private final TempStoragePort tempStoragePort;

    public Result<StoredFile> execute(UploadAvatarCommand command) {
        try {
            StoredFile tempFile = tempStoragePort.uploadFile(
                    command.inputStream(),
                    command.fileName(),
                    command.contentType(),
                    command.contentLength()
            );
            return Result.success(tempFile);
        } catch (Exception e) {
            log.error("Error uploading temp avatar: {}", e.getMessage(), e);
            return Result.error(INTERNAL_ERROR);
        }
    }
}
