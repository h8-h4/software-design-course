package app.common.event.publisher;

import app.common.event.GenericEvent;

import java.util.List;
import java.util.function.Consumer;

public interface EventPublisher {
    void publish(GenericEvent event);

    void publish(List<GenericEvent> events);

    void registerEventHandler(Class<? extends GenericEvent> type, Consumer<GenericEvent> handler);
}
