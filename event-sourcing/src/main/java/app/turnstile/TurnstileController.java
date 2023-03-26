package app.turnstile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/turnstile")
@RequiredArgsConstructor
public class TurnstileController {
    private final TurnstileService service;

    @GetMapping("in/{clientId}")
    public void in(@PathVariable String clientId) {
        if (!service.letIn(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("out/{clientId}")
    public void out(@PathVariable String clientId) {
        service.letOut(clientId);
    }
}
