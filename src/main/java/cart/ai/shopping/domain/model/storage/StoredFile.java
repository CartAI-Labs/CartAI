/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.storage;

import lombok.NonNull;

/**
 * @author Roberto Díaz
 */
public record StoredFile(
        @NonNull String id,
        @NonNull String fileName,
        @NonNull String originalFileName,
        @NonNull String fileUrl,
        @NonNull String contentType,
        String ownerId
) {
}
