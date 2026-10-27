package com.refresh.javatask.service;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestDTO.TaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task createTask(TaskRequest task) {
        Task newTask = Task.builder()
                .title(task.getTitle())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build();
        return taskRepository.save(newTask);
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
    @Transactional
    public Task updateTask(TaskRequest task, Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        existingTask.setTitle(task.getTitle());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        boolean isTaskExists = taskRepository.existsById(id);
        if (!isTaskExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
    }
}
