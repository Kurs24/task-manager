package com.refresh.javatask.service;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestdto.TaskRequest;

import java.util.List;

public interface TaskService {
    Task createTask(TaskRequest task);
    List<Task> findTasks(Task.Status status);
    Task findTaskById(Long id);
    Task updateTask(TaskRequest task, Long id);
    void deleteTask(Long id);
}
