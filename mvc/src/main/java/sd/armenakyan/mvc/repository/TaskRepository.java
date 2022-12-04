package sd.armenakyan.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sd.armenakyan.mvc.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
