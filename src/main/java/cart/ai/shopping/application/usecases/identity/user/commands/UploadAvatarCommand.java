/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.identity.user.commands;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
public record UploadAvatarCommand(
        InputStream inputStream,
        String fileName,
        String contentType,
        long contentLength
) {
}
