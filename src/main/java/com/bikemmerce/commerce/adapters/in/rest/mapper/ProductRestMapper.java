package com.bikemmerce.commerce.adapters.in.rest.mapper;

import com.bikemmerce.commerce.adapters.in.rest.dto.customer.CreateProductRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.product.UpdateProductRestRequest;
import com.bikemmerce.commerce.adapters.in.rest.dto.product.ProductRestResponse;
import com.bikemmerce.commerce.application.usecases.commands.CreateProductCommand;
import com.bikemmerce.commerce.application.usecases.commands.UpdateProductCommand;
import com.bikemmerce.commerce.domain.model.Product;

public class ProductRestMapper {

    public static CreateProductCommand toCreateProductCommand(CreateProductRestRequest request) {
        return new CreateProductCommand(
                request.name(), request.description(), request.price(), request.stock());
    }

    public static UpdateProductCommand toUpdateProductCommand(UpdateProductRestRequest request) {
        return new UpdateProductCommand(
                request.id(), request.name(), request.description(), request.price(), request.stock());
    }

    public static ProductRestResponse toResponse(Product product) {
        return new ProductRestResponse(
                product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock());
    }
}
