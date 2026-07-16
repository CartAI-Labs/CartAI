package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.ProductTranslation;
import cart.ai.shopping.domain.model.shop.events.TranslationCompletedEvent;
import cart.ai.shopping.domain.ports.shop.ProductTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
@Slf4j
public class UpdateProductTranslationsUseCase {

    private final ProductTranslationRepositoryPort translationRepositoryPort;

    public Result<List<ProductTranslation>> execute(TranslationCompletedEvent event) {
        String productId = event.getProductId();
        log.info("Updating translations for product: {}", productId);

        List<ProductTranslation> translationsToSave = new ArrayList<>();

        for (Map.Entry<String, TranslationCompletedEvent.ProductTranslationData> entry : event.getTranslations().entrySet()) {
            String languageCode = entry.getKey();
            TranslationCompletedEvent.ProductTranslationData data = entry.getValue();

            ProductTranslation translation = new ProductTranslation(
                    productId,
                    languageCode,
                    data.getName(),
                    data.getDescription(),
                    data.getAttributes()
            );

            translationsToSave.add(translation);
        }

        List<ProductTranslation> saved = translationRepositoryPort.saveAll(translationsToSave);
        log.info("Successfully saved {} translations for product {}", saved.size(), productId);

        return Result.success(saved);
    }
}
