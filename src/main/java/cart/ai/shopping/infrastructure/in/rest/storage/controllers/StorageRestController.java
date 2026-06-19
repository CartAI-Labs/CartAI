/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.storage.controllers;

import cart.ai.shopping.application.usecases.storage.DeleteFileUseCase;
import cart.ai.shopping.application.usecases.storage.DownloadFileUseCase;
import cart.ai.shopping.application.usecases.storage.UploadFileUseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.storage.vos.StoredFile;
import cart.ai.shopping.infrastructure.in.rest.storage.dtos.StorageRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageRestController {

    private final UploadFileUseCase uploadFileUseCase;
    private final DownloadFileUseCase downloadFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Result<StoredFile> result = uploadFileUseCase.execute(
                    inputStream,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    folder
            );

            if (result.hasError()) {
                return ResponseEntity.status(result.getErrorCode()).body("Could not upload file.");
            }

            StoredFile stored = result.getValue();
            StorageRestResponse response = new StorageRestResponse(
                    stored.fileUrl(),
                    stored.fileName(),
                    stored.contentType()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/files/**")
    public ResponseEntity<?> getFile(jakarta.servlet.http.HttpServletRequest request) {
        String path = request.getRequestURI().split("/api/storage/files/")[1];
        Result<InputStream> result = downloadFileUseCase.execute(path);

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("File not found.");
        }

        InputStreamResource resource = new InputStreamResource(result.getValue());

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if (path.toLowerCase().endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (path.toLowerCase().endsWith(".jpg") || path.toLowerCase().endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (path.toLowerCase().endsWith(".gif")) {
            mediaType = MediaType.IMAGE_GIF;
        }

        String fileName = path.contains("/") ? path.substring(path.lastIndexOf('/') + 1) : path;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/files/**")
    public ResponseEntity<?> deleteFile(jakarta.servlet.http.HttpServletRequest request) {
        try {
            String path = request.getRequestURI().split("/api/storage/files/")[1];
            deleteFileUseCase.execute(path);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file.");
        }
    }
}
