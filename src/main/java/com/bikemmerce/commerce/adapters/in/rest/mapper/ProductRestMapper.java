package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.request.ProductRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.response.ProductRestResponse;
import com.bikemmerce.commerce.application.usecases.commands.CreateProductCommand;
import com.bikemmerce.commerce.application.usecases.commands.UpdateProductCommand;
import com.bikemmerce.commerce.domain.model.Product;

public class ProductRestMapper {

    public static CreateProductCommand toCreateProductCommand(ProductRestRequest productRestRequest) {
        return new CreateProductCommand(
                productRestRequest.name(), productRestRequest.description(),
                productRestRequest.price(), productRestRequest.stock());
    }

    public static UpdateProductCommand toUpdateProductCommand(ProductRestRequest productRestRequest) {
        return new UpdateProductCommand(
                productRestRequest.id(), productRestRequest.name(), productRestRequest.description(),
                productRestRequest.price(), productRestRequest.stock());
    }

    public static ProductRestResponse toResponse(Product product) {
        return new ProductRestResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock());
    }
}
