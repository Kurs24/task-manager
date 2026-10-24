package com.refresh.javatask.requestDTO;

import com.refresh.javatask.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CreateTask {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Status is required")
    private Task.Status status;
    private LocalDateTime dueDate;
}
