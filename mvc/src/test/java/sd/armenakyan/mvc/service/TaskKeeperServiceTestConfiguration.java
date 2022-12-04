package sd.armenakyan.mvc.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import sd.armenakyan.mvc.repository.TaskListRepository;
import sd.armenakyan.mvc.repository.TaskRepository;

@TestConfiguration
public class TaskKeeperServiceTestConfiguration {
    @Bean
    public TaskKeeperService taskKeeperService(TaskListRepository tlr, TaskRepository tr) {
        return new TaskKeeperService(tlr, tr);
    }
}
