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
    public APIResponse<List<Task>> findTaskByStatus(Task.Status status) {
        APIResponse<List<Task>> apiResponse = new APIResponse<>();
        apiResponse.setMessage("Task successfully retrieved");
        apiResponse.setData(taskRepository.findByStatus(status));
        return apiResponse;
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
    public APIResponse<Task> updateTask(CreateTask task, Long id) {
        APIResponse<Task> apiResponse = new APIResponse<>();
        Task existingTask = taskRepository.findById(id).orElse(null);
        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setStatus(task.getStatus());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            taskRepository.save(existingTask);
            apiResponse.setMessage("Task successfully updated");
            apiResponse.setData(existingTask);
            return apiResponse;
        }
        return null;
    }

    @Override
    public APIResponse<Long> deleteTask(Long id) {
        APIResponse<Long> apiResponse = new APIResponse<>();
        taskRepository.deleteById(id);
        apiResponse.setMessage("Task successfully deleted");
        apiResponse.setData(id);
        return apiResponse;
    }
}
