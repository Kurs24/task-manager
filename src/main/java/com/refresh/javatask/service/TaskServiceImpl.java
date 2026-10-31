package com.refresh.javatask.service;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.exception.BusinessRuleException;
import com.refresh.javatask.exception.ResourceNotFoundException;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestdto.TaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task createTask(TaskRequest task) {
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new BusinessRuleException("Due date cannot be in the past");
        }
        Task newTask = Task.builder().title(task.getTitle()).status(task.getStatus()).dueDate(task.getDueDate())
                .description(task.getDescription()).build();
        return taskRepository.save(newTask);
    }

    @Override
    public List<Task> findTasks(Task.Status status) {
        if (status == null) {
            return taskRepository.findAll();
        } else {
            return taskRepository.findByStatus(status);
        }

    }

    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    @Override
    @Transactional
    public Task updateTask(TaskRequest task, Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));
        existingTask.setTitle(task.getTitle());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setDescription(task.getDescription());
        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        boolean isTaskExists = taskRepository.existsById(id);
        if (!isTaskExists) {
            throw new ResourceNotFoundException("Task", id);
        }
        taskRepository.deleteById(id);
    }
}
