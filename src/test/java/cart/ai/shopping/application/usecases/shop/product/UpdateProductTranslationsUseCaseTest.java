package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.ProductTranslation;
import cart.ai.shopping.domain.model.shop.events.TranslationCompletedEvent;
import cart.ai.shopping.domain.ports.shop.ProductTranslationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductTranslationsUseCaseTest {

    @Mock
    private ProductTranslationRepositoryPort repositoryPort;

    private UpdateProductTranslationsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductTranslationsUseCase(repositoryPort);
    }

    @Test
    void execute_shouldSaveAllTranslationsAndReturnSuccess() {
        // Arrange
        String productId = "PROD-1";
        TranslationCompletedEvent.ProductTranslationData enData = new TranslationCompletedEvent.ProductTranslationData(
                "Laptop", "Very fast", Map.of("color", "Red")
        );
        TranslationCompletedEvent.ProductTranslationData esData = new TranslationCompletedEvent.ProductTranslationData(
                "Portátil", "Muy rápido", Map.of("color", "Rojo")
        );

        TranslationCompletedEvent event = new TranslationCompletedEvent(
                productId,
                Map.of("en_US", enData, "es_ES", esData)
        );

        ProductTranslation enTranslation = new ProductTranslation(productId, "en_US", "Laptop", "Very fast", Map.of("color", "Red"));
        ProductTranslation esTranslation = new ProductTranslation(productId, "es_ES", "Portátil", "Muy rápido", Map.of("color", "Rojo"));
        
        List<ProductTranslation> expectedSaved = List.of(enTranslation, esTranslation);

        when(repositoryPort.saveAll(anyList())).thenReturn(expectedSaved);

        // Act
        Result<List<ProductTranslation>> result = useCase.execute(event);

        // Assert
        assertTrue(!result.hasError());
        assertEquals(2, result.getValue().size());
        
        verify(repositoryPort).saveAll(anyList());
    }
}
