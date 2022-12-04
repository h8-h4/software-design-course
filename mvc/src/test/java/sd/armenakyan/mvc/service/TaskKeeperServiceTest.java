package sd.armenakyan.mvc.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sd.armenakyan.mvc.exception.NotFoundException;
import sd.armenakyan.mvc.model.Task;
import sd.armenakyan.mvc.model.TaskList;
import sd.armenakyan.mvc.repository.TaskListRepository;
import sd.armenakyan.mvc.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = TaskKeeperServiceTestConfiguration.class)
class TaskKeeperServiceTest {
    @Autowired
    private TaskListRepository taskListRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskKeeperService taskKeeperService;

    private static final String LIST_NAME = "TestList";

    @Test
    public void testCreateDeleteList() {
        TaskList list = createList(LIST_NAME);

        taskKeeperService.deleteList(list.getId());
        Optional<TaskList> persistedList = taskListRepository.findByName(LIST_NAME);

        assertTrue(persistedList.isEmpty());
    }

    @Test
    public void testCreateAddTasks() {
        TaskList list = createList(LIST_NAME);

        List<String> descriptions = List.of("1", "2", "3");

        TaskList taskList = addTasks(descriptions, list);

        List<Task> persistedTasks = taskRepository.findAll();

        assertEquals(3, persistedTasks.size());
        assertEquals(descriptions.get(0), persistedTasks.get(0).getDescription());
        assertEquals(descriptions.get(1), persistedTasks.get(1).getDescription());
        assertEquals(descriptions.get(2), persistedTasks.get(2).getDescription());
        assertEquals(persistedTasks, taskList.getTasks().stream().toList());
    }

    @Test
    public void testMarkCompleted() {
        TaskList list = createList(LIST_NAME);
        List<String> tasks = List.of("1", "2", "3");
        TaskList taskList = addTasks(tasks, list);

        taskKeeperService.completeTask(taskList.getTasks().get(0).getId());
        taskKeeperService.completeTask(taskList.getTasks().get(2).getId());

        Task persistedTask1 = taskRepository.findById(taskList.getTasks().get(0).getId()).orElseThrow();
        Task persistedTask2 = taskRepository.findById(taskList.getTasks().get(1).getId()).orElseThrow();
        Task persistedTask3 = taskRepository.findById(taskList.getTasks().get(2).getId()).orElseThrow();

        assertTrue(persistedTask1.isCompleted());
        assertFalse(persistedTask2.isCompleted());
        assertTrue(persistedTask3.isCompleted());
    }


    @Test
    public void testListNotFound() {
        assertThrows(NotFoundException.class, () -> taskKeeperService.addTask(404, "task"));
    }

    @Test
    public void testTaskNotFound() {
        assertThrows(NotFoundException.class, () -> taskKeeperService.completeTask(404));
    }


    private TaskList createList(String listName) {
        TaskList list = taskKeeperService.createList(LIST_NAME);
        Optional<TaskList> persistedList = taskListRepository.findByName(LIST_NAME);

        assertTrue(persistedList.isPresent());
        assertEquals(list, persistedList.get());

        return list;
    }

    private TaskList addTasks(List<String> descriptions, TaskList list) {
        for (String desc : descriptions.subList(0, descriptions.size() - 1)) {
            taskKeeperService.addTask(list.getId(), desc);
        }
        TaskList taskList = taskKeeperService.addTask(list.getId(), descriptions.get(descriptions.size() - 1));

        TaskList persistedTaskList = taskListRepository.findById(list.getId()).orElseThrow();

        assertEquals(persistedTaskList, taskList);

        return taskList;
    }
}