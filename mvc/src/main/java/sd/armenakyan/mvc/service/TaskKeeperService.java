package sd.armenakyan.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sd.armenakyan.mvc.exception.NotFoundException;
import sd.armenakyan.mvc.model.Task;
import sd.armenakyan.mvc.model.TaskList;
import sd.armenakyan.mvc.repository.TaskListRepository;
import sd.armenakyan.mvc.repository.TaskRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskKeeperService {
    private final TaskListRepository listRepository;
    private final TaskRepository taskRepository;

    public TaskList createList(String name) {
        return listRepository.saveAndFlush(TaskList.builder().name(name).build());
    }

    public void deleteList(long listId) {
        listRepository.deleteById(listId);
    }

    public TaskList addTask(long listId, String description) {
        TaskList taskList = listRepository.findById(listId).orElseThrow(() ->
                new NotFoundException("Task list was not found.")
        );

        taskList.getTasks().add(
                Task.builder()
                        .description(description)
                        .parentList(taskList)
                        .build()
        );

        return listRepository.saveAndFlush(taskList);
    }

    public void completeTask(long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new NotFoundException("Task was not found.")
        );

        task.setCompleted(true);

        taskRepository.saveAndFlush(task);
    }

    public List<TaskList> taskLists() {
        return listRepository.findAll();
    }
}
