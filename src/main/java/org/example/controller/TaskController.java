package org.example.controller;

import org.example.domain.Status;
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
    private int tasksPerPage = 10;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "tasks_per_page", required = false, defaultValue = "0") Integer newTasksPerPage) {
        if(newTasksPerPage > 0) {
            tasksPerPage = newTasksPerPage;
        }

        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / tasksPerPage);
        if(page > totalPages) {
            page = totalPages;
        }

        model.addAttribute("current_page", page);
        model.addAttribute("tasks_per_page", tasksPerPage);
        if(page == Integer.MAX_VALUE) {
            page = totalPages;
            model.addAttribute("current_page", totalPages);
        }
        List<TaskDTO> taskDTOs = taskService.getAll((page - 1) * tasksPerPage, tasksPerPage);
        model.addAttribute("taskDTOs", taskDTOs);

        if(totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        return "tasks";
    }

    @GetMapping("/{id}")
    public String edit(Model model,
                     @PathVariable Integer id,
                       @RequestParam Integer currentPage
                    ) {
        if(isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        model.addAttribute("taskDTO", taskService.getById(id));
        model.addAttribute("current_page", currentPage);
        return "edit";

    }

    @PatchMapping("/{id}")
    public String update(Model model,
                         @ModelAttribute("taskDTO") TaskDTO taskDTO,
                         @PathVariable("id") int id,
                         @RequestParam Integer currentPage) {
        taskService.edit(id, taskDTO.getDescription(), taskDTO.getStatus());
        return tasks(model, currentPage, tasksPerPage);
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
        taskService.create(taskDTO);
        return tasks(model, Integer.MAX_VALUE, tasksPerPage);
    }

    @DeleteMapping("/{id}")
    public String delete(Model model,
                       @PathVariable Integer id,
                         @RequestParam Integer currentPage
    ) {
        if(isNull(id) || id <= 0) {
            throw new RuntimeException("Invalid id");
        }
        taskService.delete(id);
        return tasks(model, currentPage, tasksPerPage);
    }
}
