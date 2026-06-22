/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.storage.mappers;

import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.infrastructure.out.persistence.mongo.storage.documents.StoredFileDocument;

/**
 * @author Roberto Díaz
 */
public class StoredFileMapper {

    public static StoredFile toDomain(StoredFileDocument document) {
        if (document == null) {
            return null;
        }
        return new StoredFile(
                document.getId(),
                document.getFileName(),
                document.getOriginalFileName(),
                document.getFileUrl(),
                document.getContentType(),
                document.getOwnerId()
        );
    }

    public static StoredFileDocument toDocument(StoredFile domain) {
        if (domain == null) {
            return null;
        }
        return StoredFileDocument.builder()
                .id(domain.id())
                .fileName(domain.fileName())
                .originalFileName(domain.originalFileName())
                .fileUrl(domain.fileUrl())
                .contentType(domain.contentType())
                .ownerId(domain.ownerId())
                .build();
    }
}
