/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.storage;

import cart.ai.shopping.domain.model.storage.StoredFile;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
public interface TempStoragePort {

    StoredFile uploadFile(InputStream inputStream, String fileName, String contentType, long contentLength);

    void deleteFile(String fileName);

    String getBucketName();
}
