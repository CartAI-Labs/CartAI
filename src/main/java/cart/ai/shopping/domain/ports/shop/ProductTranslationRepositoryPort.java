/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.ports.shop;

import cart.ai.shopping.domain.model.shop.ProductTranslation;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port to interact with Product Translation persistence.
 *
 * @author Roberto Díaz
 */
public interface ProductTranslationRepositoryPort {
    ProductTranslation save(ProductTranslation translation);

    List<ProductTranslation> saveAll(List<ProductTranslation> translations);

    List<ProductTranslation> findByProductId(String productId);

    Optional<ProductTranslation> findByProductIdAndLanguageCode(String productId, String languageCode);
}
