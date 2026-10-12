package com.refresh.javatask.service;

import com.refresh.javatask.entity.APIResponse;
import com.refresh.javatask.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    List<Task> findTaskByStatus(Task.Status status);
    APIResponse<Task> findTaskById(Long id);
    Task updateTask(Task task);
    void deleteTask(Long id);
}
