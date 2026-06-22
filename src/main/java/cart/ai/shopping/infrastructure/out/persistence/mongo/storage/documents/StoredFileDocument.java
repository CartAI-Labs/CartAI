/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.storage.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Roberto Díaz
 */
@Document(collection = "stored_files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoredFileDocument {

    @Id
    private String id;
    private String fileName;
    private String fileUrl;
    private String contentType;
    private String ownerId;

}
