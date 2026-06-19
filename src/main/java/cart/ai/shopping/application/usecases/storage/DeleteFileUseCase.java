/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import lombok.RequiredArgsConstructor;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class DeleteFileUseCase {

    private final StoragePort storagePort;

    public void execute(String fileName) {
        if (fileName != null && !fileName.isBlank()) {
            storagePort.deleteFile(fileName);
        }
    }
}
