/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.Product;
import cart.ai.shopping.domain.model.shop.events.TranslationRequestedEvent;
import cart.ai.shopping.domain.ports.shop.TranslationEventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class TranslateProductUseCase {

    private final GetProductUseCase getProductUseCase;
    private final TranslationEventPublisherPort translationEventPublisherPort;

    @Value("${cartai.settings.languages}")
    private List<String> targetLanguages;

    public Result<Void> execute(String productId) {
        Result<Product> result = getProductUseCase.execute(productId);

        if (result.hasError()) {
            return Result.error(result.getError());
        }

        Product product = result.getValue();

        TranslationRequestedEvent event = new TranslationRequestedEvent(
                product.getId().value(),
                product.getName(),
                product.getDescription(),
                product.getAttributes(),
                targetLanguages
        );

        translationEventPublisherPort.publishTranslationRequested(event);

        return Result.success(null);
    }
}
