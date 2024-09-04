package org.example.controller;

import org.example.domain.Status;
import org.example.domain.Task;
import org.example.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        List<Task> tasks = taskService.getAll((page - 1) * limit, limit);
        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);
        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / limit);
        if(page == Integer.MAX_VALUE) {
            model.addAttribute("current_page", totalPages);
        }
        if(totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        return "tasks";

    }

    @GetMapping("/{id}")
    public String edit(Model model,
                     @PathVariable Integer id
                    //,
                    // @RequestBody TaskDTO taskDTO
                    ) {
        if(isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        model.addAttribute("task", taskService.getById(id));
        return "edit";

    }

    @PatchMapping("/{id}")
    public String update(Model model,
                         @ModelAttribute("taskDTO") TaskDTO taskDTO,
                         @PathVariable("id") int id) {
        taskService.edit(id, taskDTO.getDescription(), taskDTO.getStatus());
        return tasks(model, 1, 10);
    }

    @PostMapping("/add_page")
    public String goToAddPage(Model model) {
        Status status = Status.values()[0];
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStatus(status);
        model.addAttribute("taskDTO", taskDTO);
        return "add";
    }

    @PostMapping("/add_new_task")
    public String add(Model model,
                      @ModelAttribute("taskDTO") TaskDTO taskDTO) {
                     //@RequestBody TaskDTO taskDTO) {
        taskService.create(taskDTO.getDescription(), taskDTO.getStatus());
        return tasks(model, 1, 10);
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                       @PathVariable Integer id,
                         @RequestParam String currentPage
    ) {
        if(isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        int currentPageInt = Integer.parseInt(currentPage);
        if(currentPageInt < 1) {
            currentPageInt = 1;
        }

        return tasks(model, currentPageInt, 10);
    }
}
