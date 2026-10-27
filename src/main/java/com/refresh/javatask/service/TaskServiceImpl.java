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
    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    @Override
    public Task updateTask(CreateTask task, Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        existingTask.setTitle(task.getTitle());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        boolean isTaskExists = taskRepository.existsById(id);
        if (!isTaskExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
    }
}
