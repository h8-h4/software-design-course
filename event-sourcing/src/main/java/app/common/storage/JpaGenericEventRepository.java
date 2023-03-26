package app.common.storage;

import app.common.event.GenericEvent;
import app.common.event.repository.GenericEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaGenericEventRepository implements GenericEventRepository {
    private final EventRepository<Event> jpaRepository;

    @Override
    public void save(GenericEvent event) {
        if (event instanceof Event jpaEvent) {
            jpaRepository.save(jpaEvent);
        }
        log.warn("Unsupported event type {}", event.getClass());
    }
}
