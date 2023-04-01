package reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactive.controller.UserController;
import reactive.dto.RegisterUserRequest;
import reactive.model.Currency;
import reactive.model.User;
import reactive.repository.UserRepository;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("ConstantConditions")
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UserIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    private UserController userController;

    @BeforeEach
    public void init() {
        userController = new UserController(userRepository);
    }

    @Test
    public void testUserRegistration() {
        final RegisterUserRequest request = new RegisterUserRequest("user", Currency.RUB);
        final User user = userController.register(request).block();

        assertNotNull(user.getId());
        assertEquals(request.getName(), user.getName());
        assertEquals(request.getCurrency(), user.getCurrency());

        StepVerifier.create(userRepository.findById(user.getId()))
                .consumeNextWith(persisted -> assertEquals(persisted, user))
                .verifyComplete();
    }
}
