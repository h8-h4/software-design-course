package app.manager.repository;

import app.common.storage.EventRepository;
import app.manager.events.UpdateSubscriptionEvent;

import java.util.List;

public interface UpdateSubscriptionEventRepository extends EventRepository<UpdateSubscriptionEvent> {
    List<UpdateSubscriptionEvent> findAllByClientIdOrderByCreatedAt(String clientId);
}
