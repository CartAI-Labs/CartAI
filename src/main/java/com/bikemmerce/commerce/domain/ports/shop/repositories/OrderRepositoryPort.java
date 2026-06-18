package com.bikemmerce.commerce.domain.ports.shop.repositories;

import com.bikemmerce.commerce.domain.model.shop.Order;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderId;

import java.util.List;

public interface OrderRepositoryPort {

    void delete(OrderId orderId);

    Order find(OrderId orderId);

    List<Order> findAll();

    Order save(Order order);

}