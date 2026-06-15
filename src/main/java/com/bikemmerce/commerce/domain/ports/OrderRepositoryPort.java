package com.bikemmerce.commerce.domain.ports;

import com.bikemmerce.commerce.domain.model.Order;
import com.bikemmerce.commerce.domain.model.value.objects.OrderId;

import java.util.List;

public interface OrderRepositoryPort {

    void delete(OrderId orderId);

    Order find(OrderId orderId);

    List<Order> findAll();

    Order save(Order order);

}