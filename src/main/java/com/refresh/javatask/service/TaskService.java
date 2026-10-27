package com.refresh.javatask.service;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestDTO.TaskRequest;

import java.util.List;

public interface TaskService {
    Task createTask(TaskRequest task);
    List<Task> findTaskByStatus(Task.Status status);
    Task findTaskById(Long id);
    Task updateTask(TaskRequest task, Long id);
    void deleteTask(Long id);
}
