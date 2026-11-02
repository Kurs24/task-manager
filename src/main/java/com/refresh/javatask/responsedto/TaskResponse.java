package com.refresh.javatask.responsedto;

import com.refresh.javatask.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Task Data returned by API")
public class TaskResponse {
    @Schema(description = "Id of the task", example = "1")
    private Long id;

    @Schema(description = "Title of the task", example = "Task Title")
    private String title;

    @Schema(description = "Description of the task", example = "Task Description")
    private String description;

    @Schema(description = "Status of the task", example = "TODO")
    private Task.Status status;

    @Schema(description = "Last updated time of the task", example = "2026-10-31T04:26:52.281Z")
    private LocalDateTime updatedAt;

    @Schema(description = "Due date of the task", example = "2030-10-31T04:26:52.281Z")
    private LocalDateTime dueDate;
}
