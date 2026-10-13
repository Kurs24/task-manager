package com.refresh.javatask.service;

import com.refresh.javatask.entity.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestDTO.CreateTask;

import java.util.List;

public interface TaskService {
    APIResponse<Task> createTask(CreateTask task);
    APIResponse<List<Task>> findTaskByStatus(Task.Status status);
    APIResponse<Task> findTaskById(Long id);
    Task updateTask(Task task);
    void deleteTask(Long id);
}
