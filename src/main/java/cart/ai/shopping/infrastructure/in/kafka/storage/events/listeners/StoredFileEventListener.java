/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.kafka.storage.events.listeners;

import cart.ai.shopping.domain.model.storage.vos.StoredFileEvent;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Roberto Díaz
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StoredFileEventListener {

    private final StoragePort storagePort;
    private final TempStoragePort tempStoragePort;

    @KafkaListener(topics = "storage-uploaded-topic", groupId = "storage-processor")
    public void confirmUploadedFile(StoredFileEvent event) {
        try {
            storagePort.promoteFile(event.fileName(), tempStoragePort.getBucketName());
            log.debug("Promoted file from temp to permanent successfully: {}", event.fileName());
        } catch (Exception e) {
            log.error("Could not promote temp file to permanent: {}", event.fileName(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "storage-deleted-topic", groupId = "storage-processor")
    public void deletePhysicalFile(StoredFileEvent event) {
        try {
            storagePort.deleteFile(event.fileName());
            log.debug("Deleted physical file successfully: {}", event.fileName());
        } catch (Exception e) {
            log.error("Could not delete physical file: {}", event.fileName(), e);
            throw e;
        }
    }
}
