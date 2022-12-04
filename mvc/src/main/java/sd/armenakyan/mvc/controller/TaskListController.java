package sd.armenakyan.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sd.armenakyan.mvc.model.Task;
import sd.armenakyan.mvc.model.TaskList;
import sd.armenakyan.mvc.service.TaskKeeperService;

@Controller
@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@RequiredArgsConstructor
public class TaskListController {
    private final TaskKeeperService taskKeeperService;

    @GetMapping
    public String mainPage(ModelMap model) {
        model.addAttribute("newTaskList", new TaskList());
        model.addAttribute("newTask", new Task());
        model.addAttribute("all", taskKeeperService.taskLists());

        return "index";
    }

    @PostMapping("/add-task-list")
    public String addTaskList(@ModelAttribute("newTaskList") TaskList newTaskList) {
        taskKeeperService.createList(newTaskList.getName());
        return "redirect:/";
    }


    @DeleteMapping("/delete-task-list/{id}")
    public String deleteTaskList(@PathVariable long id) {
        taskKeeperService.deleteList(id);
        return "redirect:/";
    }

    @PostMapping("/add-task/{list_id}")
    public String addTask(@PathVariable("list_id") long listId, @ModelAttribute("newTask") Task task) {
        taskKeeperService.addTask(listId, task.getDescription());
        return "redirect:/";
    }

    @PostMapping("/complete-task/{id}")
    public String completeTask(@PathVariable("id") long id, @ModelAttribute("task") Task task) {
        taskKeeperService.completeTask(task.getId());
        return "redirect:/";
    }

}
