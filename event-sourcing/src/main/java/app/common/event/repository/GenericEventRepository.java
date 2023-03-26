package app.common.event.repository;

import app.common.event.GenericEvent;


public interface GenericEventRepository {
    void save(GenericEvent event);
}
