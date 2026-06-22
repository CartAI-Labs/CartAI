/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.storage.dtos;

/**
 * @author Roberto Díaz
 */
public record StorageRestResponse(
        String id,
        String originalFileName,
        String fileUrl,
        String contentType
) {
}
