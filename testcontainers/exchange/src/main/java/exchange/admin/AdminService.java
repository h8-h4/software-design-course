package exchange.admin;

import exchange.dto.RegisterUserRequest;
import exchange.model.User;
import exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public User register(RegisterUserRequest request) {
        return userRepository.save(
                User.builder()
                        .id(UUID.randomUUID().toString())
                        .balance(request.getBalance())
                        .build()
        );
    }

    public Optional<User> addBalance(String userId, double balance) {
        return userRepository.findById(userId)
                .map(user -> userRepository.save(user.withBalance(user.getBalance() + balance)));
    }

    public Optional<Double> getBalance(String userId) {
        return userRepository.findById(userId).map(User::getBalance);
    }

}
