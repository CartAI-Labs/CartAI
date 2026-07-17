/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.persistence.mongo.shop.mappers;

import cart.ai.shopping.domain.model.shop.ProductTranslation;
import cart.ai.shopping.infrastructure.out.persistence.mongo.shop.documents.ProductTranslationDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Roberto Díaz
 */
@Component
public class ProductTranslationMapper {

    public ProductTranslation toDomain(ProductTranslationDocument document) {
        if (document == null) return null;

        return new ProductTranslation(
                document.getProductId(),
                document.getLanguageCode(),
                document.getName(),
                document.getDescription(),
                document.getAttributes()
        );
    }

    public ProductTranslationDocument toDocument(ProductTranslation domain) {
        if (domain == null) return null;

        return ProductTranslationDocument.builder()
                .productId(domain.getProductId())
                .languageCode(domain.getLanguageCode())
                .name(domain.getName())
                .description(domain.getDescription())
                .attributes(domain.getAttributes())
                .build();
    }

    public List<ProductTranslation> toDomainList(List<ProductTranslationDocument> documents) {
        if (documents == null) return null;
        return documents.stream().map(this::toDomain).collect(Collectors.toList());
    }

    public List<ProductTranslationDocument> toDocumentList(List<ProductTranslation> domains) {
        if (domains == null) return null;
        return domains.stream().map(this::toDocument).collect(Collectors.toList());
    }
}
