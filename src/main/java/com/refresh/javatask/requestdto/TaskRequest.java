package com.refresh.javatask.requestdto;

import com.refresh.javatask.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Data for creating or updating a task")
public class TaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    @Schema(description = "Title of the task, must be at most 255 characters",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "Example Task", minLength = 1)
    private String title;

    @NotNull(message = "Status is required")
    @Schema(description = "Status of the task", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "TODO")
    private Task.Status status;

    @Schema(description = "Interpreted as UTC. Must not be in the past when creating, or when changing an existing due date",
            example = "2030-12-31T23:59:59")
    private LocalDateTime dueDate;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    @Schema(description = "Description of the task, must be at most 1000 characters", example = "Example Description")
    private String description;
}