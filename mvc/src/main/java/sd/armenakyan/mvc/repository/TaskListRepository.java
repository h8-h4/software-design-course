package sd.armenakyan.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sd.armenakyan.mvc.model.TaskList;

import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    Optional<TaskList> findByName(String listName);
}
