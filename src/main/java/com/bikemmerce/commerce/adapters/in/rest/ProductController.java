package com.bikemmerce.commerce.adapters.in.rest;

import com.bikemmerce.commerce.adapters.in.rest.dto.request.ProductRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.response.ProductRestResponse;
import com.bikemmerce.commerce.adapters.in.rest.mapper.ProductRestMapper;
import com.bikemmerce.commerce.application.usecases.product.*;
import com.bikemmerce.commerce.domain.model.Product;
import com.bikemmerce.commerce.domain.model.value.objects.ProductId;
import com.bikemmerce.commerce.domain.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor // Genera el constructor para la inyección por constructor de Spring
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductUseCase listProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRestRequest productRestRequest) {
        Result<Product> result = createProductUseCase.execute(ProductRestMapper.toDomain(productRestRequest));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable String id) {
        Result<Product> result = getProductUseCase.execute(new ProductId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(result.getValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        Result<Product> result = deleteProductUseCase.execute(new ProductId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Could not delete");
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
    public ResponseEntity<?> putProduct(@RequestBody ProductRestRequest productRestRequest) {
        Result<Product> result = updateProductUseCase.execute(ProductRestMapper.toDomain(productRestRequest));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(ProductRestMapper.toResponse(result.getValue()));
    }

}
