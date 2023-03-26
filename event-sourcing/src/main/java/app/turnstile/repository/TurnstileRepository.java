package app.turnstile.repository;

import app.common.storage.EventRepository;
import app.turnstile.events.TurnstileEvent;

import java.util.List;

public interface TurnstileRepository extends EventRepository<TurnstileEvent> {
    List<TurnstileEvent> findAllByOrderByCreatedAt();
}
