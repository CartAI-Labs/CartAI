/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.usecases.shop.commands.CreateProductCommand;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.vos.ProductId;
import cart.ai.shopping.domain.ports.common.IncrementIdGeneratorPort;
import cart.ai.shopping.domain.ports.shop.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static cart.ai.shopping.domain.common.result.ResultError.INTERNAL_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Roberto Díaz
 */
@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private IncrementIdGeneratorPort incrementIdGeneratorPort;

    private CreateProductUseCase createProductUseCase;

    @BeforeEach
    void setUp() {
        createProductUseCase = new CreateProductUseCase(productRepositoryPort, incrementIdGeneratorPort);
    }

    @Test
    void shouldReturnErrorWhenProductIdAlreadyExists() {
        String generatedId = "PROD-1";
        when(incrementIdGeneratorPort.generate(Product.class)).thenReturn(generatedId);

        Product existingProduct = new Product(new ProductId(generatedId), "Laptop", "Desc", BigDecimal.TEN, 5);
        when(productRepositoryPort.find(new ProductId(generatedId))).thenReturn(existingProduct);

        CreateProductCommand command = new CreateProductCommand("New Laptop", "Desc", BigDecimal.TEN, 5, List.of());

        var result = createProductUseCase.execute(command);

        assertTrue(result.hasError(), "Expected error when product already exists");
        assertEquals(INTERNAL_ERROR, result.getError());
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void shouldCreateProductSuccessfully() {
        String generatedId = "PROD-2";
        when(incrementIdGeneratorPort.generate(Product.class)).thenReturn(generatedId);
        when(productRepositoryPort.find(new ProductId(generatedId))).thenReturn(null);
        when(productRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CreateProductCommand command = new CreateProductCommand("New Laptop", "Desc", BigDecimal.TEN, 5, List.of("img1"));

        var result = createProductUseCase.execute(command);

        assertFalse(result.hasError(), "Expected success");
        assertNotNull(result.getValue());
        assertEquals("New Laptop", result.getValue().getName());
        assertEquals(1, result.getValue().getImageFileIds().size());
        verify(productRepositoryPort).save(any());
    }
}
