/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.shop.controllers;

import cart.ai.shopping.application.usecases.shop.product.*;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.ProductTranslation;
import cart.ai.shopping.infrastructure.in.rest.common.ResultErrorHttpStatusMapper;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.ProductRestResponse;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.UpdateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.mappers.ProductRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor // Genera el constructor para la inyección por constructor de Spring
public class ProductRestController {

    private static final String DEFAULT_LANGUAGE = "en_US";

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductUseCase listProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final TranslateProductUseCase translateProductUseCase;
    private final GetProductTranslationUseCase getProductTranslationUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRestRequest request) {
        Result<Product> result = createProductUseCase.execute(ProductRestMapper.toCreateProductCommand(request));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Error.");
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id, @RequestParam(required = false) String lang) {
        Result<Product> result = getProductUseCase.execute(id);

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Not found.");
        }

        Product product = result.getValue();

        if (lang != null) {
            Optional<ProductTranslation> translation = getProductTranslationUseCase.execute(id, lang).getValue();

            if (translation.isEmpty() && !DEFAULT_LANGUAGE.equals(lang)) {
                translation = getProductTranslationUseCase.execute(id, DEFAULT_LANGUAGE).getValue();
            }

            if (translation.isPresent()) {
                return ResponseEntity.ok(ProductRestMapper.toResponse(product, translation.get()));
            }
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        Result<Product> result = deleteProductUseCase.execute(id);

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Could not delete");
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(result.getValue()));
    }

    @GetMapping
    public ResponseEntity<?> getProducts() {
        Result<List<Product>> result = listProductUseCase.execute();

        List<ProductRestResponse> productRestResponses =
                result.getValue().stream().map(ProductRestMapper::toResponse).toList();

        return ResponseEntity.ok(productRestResponses);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<?> putProduct(@RequestBody @Valid UpdateProductRestRequest request) {
        Result<Product> result = updateProductUseCase.execute(ProductRestMapper.toUpdateProductCommand(request));

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Error.");
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(result.getValue()));
    }

    @PostMapping("/{id}/translations")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<?> translateProduct(@PathVariable String id) {
        Result<Void> result = translateProductUseCase.execute(id);

        if (result.hasError()) {
            return ResponseEntity.status(ResultErrorHttpStatusMapper.toHttpStatus(result.getError())).body("Not found.");
        }

        return ResponseEntity.ok().build();
    }

}
