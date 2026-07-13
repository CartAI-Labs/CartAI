/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.domain.model.shop;

import cart.ai.shopping.domain.model.shop.vos.ProductId;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Roberto Díaz
 */
@Data
public class Product {

    @NonNull
    private ProductId id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Integer stock;
    private List<String> imageFileIds = new ArrayList<>();
    private Map<String, String> attributes = new HashMap<>();

    public Product(@NonNull ProductId id, @NonNull String name, @NonNull String description, @NonNull BigDecimal price, @NonNull Integer stock) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product(@NonNull ProductId id, @NonNull String name, @NonNull String description, @NonNull BigDecimal price, @NonNull Integer stock, List<String> imageFileIds) {
        this(id, name, description, price, stock, imageFileIds, null);
    }

    public Product(@NonNull ProductId id, @NonNull String name, @NonNull String description, @NonNull BigDecimal price, @NonNull Integer stock, List<String> imageFileIds, Map<String, String> attributes) {
        this(id, name, description, price, stock);
        if (imageFileIds != null) {
            this.imageFileIds.addAll(imageFileIds);
        }
        if (attributes != null) {
            this.attributes.putAll(attributes);
        }
    }

    public void addImage(String fileId) {
        this.imageFileIds.add(fileId);
    }

    public void setMainImage(String fileId) {
        if (this.imageFileIds.remove(fileId)) {
            this.imageFileIds.addFirst(fileId);
        } else {
            throw new IllegalArgumentException("Image not found in product");
        }
    }

    public Optional<String> getMainImageId() {
        return this.imageFileIds.isEmpty() ? Optional.empty() : Optional.of(this.imageFileIds.get(0));
    }

    public List<String> getGalleryImages() {
        return Collections.unmodifiableList(this.imageFileIds);
    }

    public void increaseStock(Integer count) {
        this.stock += count;
    }

    public void decreaseStock(Integer count) {
        this.stock -= count;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }

}
