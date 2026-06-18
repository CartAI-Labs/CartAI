package cart.ai.domain.ports.security.repositories;

import cart.ai.domain.model.security.User;
import cart.ai.domain.model.security.value.objects.Email;
import cart.ai.domain.model.security.value.objects.UserId;

import java.util.List;

public interface UserRepositoryPort {

    void delete(UserId userId);

    User findByUserId(UserId userId);

    User findByEmail(Email email);

    List<User> findAll();

    User save(User product);
}
