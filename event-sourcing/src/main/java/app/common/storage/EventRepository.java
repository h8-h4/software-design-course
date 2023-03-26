package app.common.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository<T extends Event> extends JpaRepository<T, String> {
}
