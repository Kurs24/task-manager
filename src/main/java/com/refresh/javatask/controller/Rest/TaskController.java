package com.refresh.javatask.controller.Rest;

import com.refresh.javatask.entity.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestDTO.CreateTask;
import com.refresh.javatask.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<APIResponse<Task>> createTask(@RequestBody CreateTask task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Task>>> getTask(@RequestParam Task.Status status) {
        return ResponseEntity.ok(taskService.findTaskByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> updateTask(@RequestBody CreateTask updatedTask, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(updatedTask, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> deleteTask(@PathVariable Long id) {
        APIResponse<Task> response = new APIResponse<>();
        response.setMessage("Task successfully deleted");
        response.setData(null);
        return ResponseEntity.ok(response);
    }
}
