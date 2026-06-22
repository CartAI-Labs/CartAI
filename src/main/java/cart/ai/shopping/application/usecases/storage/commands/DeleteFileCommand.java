/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.storage.commands;

import lombok.NonNull;

/**
 * @author Roberto Díaz
 */
public record DeleteFileCommand(
        @NonNull String id,
        String requesterUserId
) {
}
