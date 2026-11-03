package com.refresh.javatask.controller;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestdto.TaskRequest;
import com.refresh.javatask.responsedto.APIResponse;
import com.refresh.javatask.responsedto.TaskResponse;
import com.refresh.javatask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tasks", produces = "application/json")
@Tag(name = "task", description = "all task related operations")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(
            summary = "Create a New Task",
            description = "Create a new task with the provided details",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Response when task is successfully created"
                    )
            }
    )
    public ResponseEntity<APIResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request) {
        TaskResponse created = taskService.createTask(request);
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location)
                .body(APIResponse.of(created, "Task successfully created"));
    }

    @GetMapping
    @Operation(
            summary = "Get Task List by status",
            description = "Get a list of tasks by status, if no status is provided, all tasks are returned",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Response when task is successfully retrieved"
                    )
            }
    )
    public ResponseEntity<APIResponse<List<TaskResponse>>> getTasks(
            @Parameter(
                    description = "Status of the task",
                    in = ParameterIn.QUERY

            ) @RequestParam(required = false) Task.Status status) {
        List<TaskResponse> taskLists = taskService.findTasks(status);
        return ResponseEntity.ok().body(APIResponse.of(taskLists, "Tasks successfully retrieved"));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Task by id",
            description = "Get specific task by id, throw error if task not found",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Response when task is successfully retrieved"
                    )
            }
    )
    public ResponseEntity<APIResponse<Task>> getTaskById(@Parameter(
            description = "Id of the task",
            required = true,
            in = ParameterIn.PATH
    ) @PathVariable Long id) {
        Task foundTask = taskService.findTaskById(id);
        return ResponseEntity.ok().body(APIResponse.of(foundTask, "Task successfully retrieved"));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Edit Task by ID",
            description = "Edit specific task by id, throw error if task not found",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Response when task is successfully updated"
                    )
            }
    )
    public ResponseEntity<APIResponse<Task>> updateTask(@Valid @RequestBody TaskRequest request,
                                                        @Parameter(
                                                                description = "Id of the task",
                                                                required = true,
                                                                in = ParameterIn.PATH
                                                        ) @PathVariable Long id) {
        Task updated = taskService.updateTask(request, id);
        return ResponseEntity.ok(APIResponse.of(updated, "Task successfully updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Task by ID",
            description = "Delete specific task by id, throw error if task not found",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Response when task is successfully deleted"
                    )
            }
    )
    public ResponseEntity<Void> deleteTask(
            @Parameter(
                    description = "Id of the task",
                    required = true,
                    in = ParameterIn.PATH
            ) @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
