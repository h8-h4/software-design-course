package app.common.event.applier;

import app.common.event.GenericEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ApplierContainer<T> {
    private final Map<Class<? extends GenericEvent>, ApplierFunction<T>> appliers = new HashMap<>();

    public T apply(T object, List<? extends GenericEvent> events) {
        return apply(object, events.toArray(GenericEvent[]::new));
    }

    public T apply(T object, GenericEvent... events) {
        for (GenericEvent event : events) {
            if (appliers.containsKey(event.getClass())) {
                object = appliers.get(event.getClass()).apply(event, object);
            }
        }
        return object;
    }

    public void register(Class<? extends GenericEvent> type, ApplierFunction<T> applierFunction) {
        appliers.put(type, applierFunction);
    }
}
