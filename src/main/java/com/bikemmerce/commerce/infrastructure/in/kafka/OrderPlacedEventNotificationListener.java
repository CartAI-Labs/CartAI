package com.bikemmerce.commerce.infrastructure.in.kafka;

import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.model.shop.value.objects.CustomerAddedEvent;
import com.bikemmerce.commerce.domain.model.shop.value.objects.OrderPlacedEvent;
import com.bikemmerce.commerce.domain.ports.shop.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventNotificationListener {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    @KafkaListener(topics = "orders-topic", groupId = "email-notification")
    public void notify(OrderPlacedEvent orderPlacedEvent) {

        Customer customer = customerRepositoryPort.findByCustomerId(orderPlacedEvent.userId());

        log.info("email sent for {} to {}", orderPlacedEvent.orderId(), customer.email().value());
    }

    @KafkaListener(topics = "orders-topic", groupId = "post-processor")
    public void postProcess(CustomerAddedEvent event) {
        Cart cart = cartRepositoryPort.find(event.userId());

        if (cart == null) {
            return;
        }

        cart.clearItems();

        cartRepositoryPort.save(cart);
    }
}
