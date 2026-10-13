package com.refresh.javatask.service;

import com.refresh.javatask.entity.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestDTO.CreateTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public APIResponse<Task> createTask(CreateTask task) {
        APIResponse<Task> apiResponse = new APIResponse<>();
        Task newTask = Task.builder()
                .title(task.getTitle())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
                .updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        taskRepository.save(newTask);
        apiResponse.setMessage("Task successfully created");
        apiResponse.setData(newTask);
        return apiResponse;
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
