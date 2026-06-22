/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.storage;

import cart.ai.shopping.domain.model.storage.StoredFile;

/**
 * @author Roberto Díaz
 */
public interface StoredFileRepositoryPort {

    StoredFile save(StoredFile storedFile);

    StoredFile findById(String id);

    void deleteById(String id);
}
