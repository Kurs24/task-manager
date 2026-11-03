package com.refresh.javatask.mapper;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.responsedto.TaskResponse;

public class TaskMapper {
    private TaskMapper() {
        /* This utility class should not be instantiated */
    }

    public static TaskResponse toTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setUpdatedAt(task.getUpdatedAt());
        taskResponse.setDueDate(task.getDueDate());
        return taskResponse;
    }
}
