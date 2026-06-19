/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.ports.storage.StoragePort;
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

    public Result<InputStream> execute(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return Result.error(HttpStatus.BAD_REQUEST.value());
        }

        try {
            InputStream inputStream = storagePort.downloadFile(fileName);
            return Result.success(inputStream);
        } catch (Exception e) {
            return Result.error(HttpStatus.NOT_FOUND.value());
        }
    }
}
