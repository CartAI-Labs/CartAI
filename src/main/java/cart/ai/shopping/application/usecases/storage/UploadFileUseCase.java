/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.storage.vos.StoredFile;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class UploadFileUseCase {

    private final StoragePort storagePort;

    public Result<StoredFile> execute(InputStream inputStream, String originalFileName, String contentType, long contentLength) {
        return execute(inputStream, originalFileName, contentType, contentLength, null);
    }

    public Result<StoredFile> execute(InputStream inputStream, String originalFileName, String contentType, long contentLength, String folder) {
        if (inputStream == null || originalFileName == null || originalFileName.isBlank()) {
            return Result.error(HttpStatus.BAD_REQUEST.value());
        }

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFileName.substring(dotIndex);
        }

        String uniqueFileName = UUID.randomUUID() + extension;
        String finalKey = uniqueFileName;
        if (folder != null && !folder.isBlank()) {
            String sanitizedFolder = folder.trim();
            if (sanitizedFolder.endsWith("/")) {
                finalKey = sanitizedFolder + uniqueFileName;
            } else {
                finalKey = sanitizedFolder + "/" + uniqueFileName;
            }
        }

        try {
            StoredFile storedFile = storagePort.uploadFile(inputStream, finalKey, contentType, contentLength);
            return Result.success(storedFile);
        } catch (Exception e) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
