package org.example.service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.TaskDTO;
import org.example.dao.TaskDAO;
import org.example.domain.Status;
import org.example.domain.Task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskDAO taskDAO;
    ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<TaskDTO> getAll(int offset, int limit) {
        List<TaskDTO> list = new ArrayList<>();
        taskDAO.getAll(offset, limit).forEach(task -> list.add(objectMapper.convertValue(task, TaskDTO.class)));
        return list;
    }

    public int getAllCount() {
        return taskDAO.getAllCount();
    }

    public TaskDTO getById(int id) {
        return objectMapper.convertValue(taskDAO.getById(id), TaskDTO.class);
    }

    @Transactional
    public Task edit(int id, String description, Status status) {
        Task task = taskDAO.getById(id);
        if(isNull(task)){
            throw new RuntimeException("Task not found");
        }

        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);
        return task;
    }

    public Task create(TaskDTO taskDTO) {
        Task task = objectMapper.convertValue(taskDTO, Task.class);
        taskDAO.saveOrUpdate(task);
        return task;
    }

    @Transactional
    public void delete(int id) {
        Task task = taskDAO.getById(id);
        if(isNull(task)){
            throw new RuntimeException("Task not found");
        }

        taskDAO.delete(task);
    }


}
