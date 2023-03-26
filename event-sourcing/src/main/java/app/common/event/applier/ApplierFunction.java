package app.common.event.applier;

import app.common.event.GenericEvent;

@FunctionalInterface
public interface ApplierFunction<T> {
    T apply(GenericEvent event, T t);
}
