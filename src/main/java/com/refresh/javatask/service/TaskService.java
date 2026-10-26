package com.refresh.javatask.service;

import com.refresh.javatask.ResponseDTO.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestDTO.CreateTask;

import java.util.List;

public interface TaskService {
    Task createTask(CreateTask task);
    List<Task> findTaskByStatus(Task.Status status);
    Task findTaskById(Long id);
    APIResponse<Task> updateTask(CreateTask task, Long id);
    void deleteTask(Long id);
}
