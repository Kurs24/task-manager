package com.refresh.javatask.controller.Rest;

import com.refresh.javatask.ResponseDTO.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestDTO.CreateTask;
import com.refresh.javatask.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<APIResponse<Task>> createTask(@Valid @RequestBody CreateTask task) {
        Task created = taskService.createTask(task);
        URI location = URI.create("/api/tasks/" + created.getId());
        return ResponseEntity.created(location)
                .body(APIResponse.of(created, "Task successfully created"));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Task>>> getTask(@RequestParam(required = false) Task.Status status) {
        return ResponseEntity.ok(taskService.findTaskByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> updateTask(@Valid @RequestBody CreateTask updatedTask,
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(updatedTask, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
