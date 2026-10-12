package com.refresh.javatask.service;

import com.refresh.javatask.entity.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        return null;
    }

    @Override
    public List<Task> findTaskByStatus(Task.Status status) {
        return List.of();
    }

    @Override
    public APIResponse<Task> findTaskById(Long id) {
        APIResponse<Task> apiResponse = new APIResponse<>();
        Task task = taskRepository.findById(id).orElse(null);
        apiResponse.setMessage("Task successfully retrieved");
        apiResponse.setData(task);
        return apiResponse;
    }

    @Override
    public Task updateTask(Task task) {
        return null;
    }

    @Override
    public void deleteTask(Long id) {

    }
}
