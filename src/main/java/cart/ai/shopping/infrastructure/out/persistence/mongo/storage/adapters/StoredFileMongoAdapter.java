/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.storage.adapters;

import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.storage.StoredFileRepositoryPort;
import cart.ai.shopping.infrastructure.out.persistence.mongo.storage.documents.StoredFileDocument;
import cart.ai.shopping.infrastructure.out.persistence.mongo.storage.mappers.StoredFileMapper;
import cart.ai.shopping.infrastructure.out.persistence.mongo.storage.repositories.StoredFileMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
public class StoredFileMongoAdapter implements StoredFileRepositoryPort {

    private final StoredFileMongoRepository repository;

    @Override
    public StoredFile save(StoredFile storedFile) {
        StoredFileDocument document = StoredFileMapper.toDocument(storedFile);
        StoredFileDocument saved = repository.save(document);
        return StoredFileMapper.toDomain(saved);
    }

    @Override
    public StoredFile findById(String id) {
        return repository.findById(id)
                .map(StoredFileMapper::toDomain)
                .orElse(null);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
