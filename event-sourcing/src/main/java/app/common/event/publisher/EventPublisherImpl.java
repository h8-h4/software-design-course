package app.common.event.publisher;

import app.common.event.GenericEvent;
import app.common.event.repository.GenericEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisherImpl implements EventPublisher {
    private final Map<Class<? extends GenericEvent>, List<Consumer<GenericEvent>>> eventHandlers = new HashMap<>();

    private final GenericEventRepository eventRepository;

    @Override
    public void publish(GenericEvent event) {
        final List<Consumer<GenericEvent>> handlers = eventHandlers.get(event.getClass());

        if (handlers == null || handlers.isEmpty()) {
            log.warn("No handlers registered for event type {}", event.getClass());
            eventRepository.save(event);
            return;
        }

        handlers.forEach(it -> it.accept(event));
        eventRepository.save(event);
    }

    @Override
    public void publish(List<GenericEvent> events) {
        events.forEach(this::publish);
    }

    @Override
    public void registerEventHandler(Class<? extends GenericEvent> type, Consumer<GenericEvent> handler) {
        eventHandlers.putIfAbsent(type, new ArrayList<>());
        eventHandlers.get(type).add(handler);
    }
}
