package com.refresh.javatask.requestDTO;

import com.refresh.javatask.entity.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CreateTask {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Status is required")
    private Task.Status status;

    @NotBlank(message = "Due date is required")
    private LocalDateTime dueDate;
}
