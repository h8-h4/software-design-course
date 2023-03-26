package exchange.admin;

import exchange.dto.RegisterUserRequest;
import exchange.dto.UserResponse;
import exchange.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final AdminService adminService;

    @PostMapping(value = "/register")
    public UserResponse register(@RequestBody RegisterUserRequest registerUserRequest) {
        return toResponse(adminService.register(registerUserRequest));
    }

    @PostMapping(value = "/{user_id}/balance")
    public UserResponse addBalance(
            @PathVariable("user_id") String userId,
            @RequestParam("balance") double balance
    ) {
        if (balance <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return toResponse(adminService.addBalance(userId, balance).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        ));
    }

    @GetMapping("/{user_id}/balance")
    public double getBalance(@PathVariable("user_id") String userId) {
        return adminService.getBalance(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getBalance());
    }
}
