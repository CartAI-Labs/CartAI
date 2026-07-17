/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.application.annotations.UseCase;
import cart.ai.shopping.domain.common.result.Result;
import cart.ai.shopping.domain.model.shop.ProductTranslation;
import cart.ai.shopping.domain.ports.shop.ProductTranslationRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@RequiredArgsConstructor
@UseCase
public class GetProductTranslationUseCase {

    private final ProductTranslationRepositoryPort productTranslationRepositoryPort;

    public Result<Optional<ProductTranslation>> execute(String productId, String languageCode) {
        Optional<ProductTranslation> translation =
                productTranslationRepositoryPort.findByProductIdAndLanguageCode(productId, languageCode);

        return Result.success(translation);
    }
}
