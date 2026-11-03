package com.refresh.javatask.service;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestdto.TaskRequest;
import com.refresh.javatask.responsedto.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest task);
    List<TaskResponse> findTasks(Task.Status status);
    TaskResponse findTaskById(Long id);
    Task updateTask(TaskRequest task, Long id);
    void deleteTask(Long id);
}
