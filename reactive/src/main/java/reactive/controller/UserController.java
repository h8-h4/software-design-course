package reactive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactive.dto.RegisterUserRequest;
import reactive.model.User;
import reactive.repository.UserRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping
    public Mono<User> register(@RequestBody RegisterUserRequest registerUserRequest) {
        return userRepository.save(
                User.builder()
                        .id(UUID.randomUUID().toString())
                        .name(registerUserRequest.getName())
                        .currency(registerUserRequest.getCurrency())
                        .build()
        );
    }
}
