package com.refresh.javatask.service;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.exception.BusinessRuleException;
import com.refresh.javatask.exception.ResourceNotFoundException;
import com.refresh.javatask.mapper.TaskMapper;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestdto.TaskRequest;
import com.refresh.javatask.responsedto.TaskResponse;
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
    public TaskResponse createTask(TaskRequest task) {
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new BusinessRuleException("Due date cannot be in the past");
        }
        Task newTask = Task.builder().title(task.getTitle()).status(task.getStatus()).dueDate(task.getDueDate())
                .description(task.getDescription()).build();
        return TaskMapper.toTaskResponse(taskRepository.save(newTask));
    }

    @Override
    public List<TaskResponse> findTasks(Task.Status status) {
        if (status == null) {
            return taskRepository.findAll().stream().map(TaskMapper::toTaskResponse).toList();
        } else {
            return taskRepository.findByStatus(status).stream().map(TaskMapper::toTaskResponse).toList();
        }

    }

    @Override
    public TaskResponse findTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));
        return TaskMapper.toTaskResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(TaskRequest task, Long id) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", id));

        if(task.getDueDate() != null && !task.getDueDate().equals(existingTask.getDueDate()) &&
                    task.getDueDate().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
                throw new BusinessRuleException("Due date cannot be in the past");
            }

        existingTask.setTitle(task.getTitle());
        existingTask.setStatus(task.getStatus());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setDescription(task.getDescription());
        return TaskMapper.toTaskResponse(taskRepository.saveAndFlush(existingTask));
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
