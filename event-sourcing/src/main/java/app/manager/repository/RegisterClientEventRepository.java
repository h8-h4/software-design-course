package app.manager.repository;

import app.common.storage.EventRepository;
import app.manager.events.RegisterClientEvent;

import java.util.Optional;

public interface RegisterClientEventRepository extends EventRepository<RegisterClientEvent> {
    Optional<RegisterClientEvent> findByClientId(String id);
}
