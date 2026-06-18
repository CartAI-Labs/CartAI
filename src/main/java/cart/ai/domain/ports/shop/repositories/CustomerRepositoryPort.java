package cart.ai.domain.ports.shop.repositories;

import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.model.security.value.objects.UserId;
import cart.ai.domain.model.shop.Customer;

import java.util.List;

public interface CustomerRepositoryPort {

    void delete(UserId userId);

    Customer findByCustomerId(UserId userId);

    Customer findByEmail(Email email);

    List<Customer> findAll();

    Customer save(Customer product);

}