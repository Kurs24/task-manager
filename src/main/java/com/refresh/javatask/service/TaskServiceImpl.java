package com.refresh.javatask.service;

import com.refresh.javatask.ResponseDTO.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestDTO.CreateTask;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(CreateTask task) {
        Task newTask = Task.builder()
                .title(task.getTitle())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .createdAt(LocalDateTime.now(ZoneId.of("UTC")))
                .updatedAt(LocalDateTime.now(ZoneId.of("UTC")))
                .build();
        taskRepository.save(newTask);
        return newTask;
    }

    @Override
    public List<Task> findTaskByStatus(Task.Status status) {
        if (status == null) {
            return taskRepository.findAll();
        } else {
            return taskRepository.findByStatus(status);
        }

    }

    @Override
    public APIResponse<Task> findTaskById(Long id) {
        APIResponse<Task> apiResponse = new APIResponse<>();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        apiResponse.setMessage("Task successfully retrieved");
        apiResponse.setData(task);
        return apiResponse;
    }

    @Override
    public APIResponse<Task> updateTask(CreateTask task, Long id) {
        APIResponse<Task> apiResponse = new APIResponse<>();
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        existingTask.setTitle(task.getTitle());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        taskRepository.save(existingTask);
        apiResponse.setMessage("Task successfully updated");
        apiResponse.setData(existingTask);
        return apiResponse;

    }

    @Override
    public void deleteTask(Long id) {
        APIResponse<Long> apiResponse = new APIResponse<>();
        boolean isTaskExists = taskRepository.existsById(id);
        if (!isTaskExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
    }
}
