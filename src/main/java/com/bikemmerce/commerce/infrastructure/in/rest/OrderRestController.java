package com.bikemmerce.commerce.infrastructure.in.rest;

import com.bikemmerce.commerce.application.usecases.shop.order.CancelOrderUseCase;
import com.bikemmerce.commerce.application.usecases.shop.order.CreateOrderUseCase;
import com.bikemmerce.commerce.application.usecases.shop.order.GetOrderUseCase;
import com.bikemmerce.commerce.domain.model.security.value.objects.UserId;
import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderId;
import com.bikemmerce.commerce.domain.result.Result;
import com.bikemmerce.commerce.infrastructure.in.rest.mapper.OrderRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;


    @PostMapping("/{id}")
    public ResponseEntity<?> createOrder(@PathVariable String id) {
        Result<Order> result = createOrderUseCase.execute(new UserId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Error.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Result<Order> result = getOrderUseCase.execute(new OrderId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        Result<Order> result = cancelOrderUseCase.execute(new OrderId(id));

        if (result.hasError()) {
            return ResponseEntity.status(result.getErrorCode()).body("Not found.");
        }

        return ResponseEntity.ok(OrderRestMapper.toResponse(result.getValue()));
    }
}
