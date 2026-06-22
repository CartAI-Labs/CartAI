/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage.commands;

import lombok.NonNull;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
public record UploadFileCommand(
        @NonNull InputStream inputStream,
        @NonNull String originalFileName,
        @NonNull String contentType,
        long contentLength,
        String ownerId,
        String requesterUserId
) {
}
