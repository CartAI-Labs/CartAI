package com.bikemmerce.commerce.infrastructure.in.kafka;

import com.bikemmerce.commerce.domain.model.shop.Cart;
import com.bikemmerce.commerce.domain.model.shop.Customer;
import com.bikemmerce.commerce.domain.model.shop.value.objects.CustomerAddedEvent;
import com.bikemmerce.commerce.domain.ports.shop.CartRepositoryPort;
import com.bikemmerce.commerce.domain.ports.shop.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerAddedEventListener {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;


    @KafkaListener(topics = "customers-topic", groupId = "email-notification")
    public void notify(CustomerAddedEvent event) {

        Customer customer = customerRepositoryPort.findByCustomerId(event.userId());

        log.info("email sent to {}", customer.name());
    }

    @KafkaListener(topics = "customers-topic", groupId = "post-processor")
    public void postProcess(CustomerAddedEvent event) {
        Cart cart = cartRepositoryPort.find(event.userId());

        if (cart != null) {
            return;
        }

        cart = cartRepositoryPort.save(new Cart(event.userId(), new ArrayList<>()));
        cartRepositoryPort.save(cart);
    }
}
