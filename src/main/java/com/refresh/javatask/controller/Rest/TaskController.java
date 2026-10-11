package com.refresh.javatask.controller.Rest;

import com.refresh.javatask.entity.APIResponse;
import com.refresh.javatask.entity.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @PostMapping
    public ResponseEntity<APIResponse<String>> createTask() {
        /* TODO: Implement createTask */
        APIResponse<String> response = new APIResponse<>();
        response.setMessage("Task created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Task>>> getTask(@RequestParam Task.Status status) {
        APIResponse<List<Task>> response = new APIResponse<>();
        response.setMessage("Task successfully retrieved");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<APIResponse<Task>> getTaskById(@PathVariable Long id) {
        APIResponse<Task> response = new APIResponse<>();
        response.setMessage("Task successfully retrieved");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<APIResponse<Task>> updateTask(@RequestBody Task newTask, @PathVariable Long id) {
        APIResponse<Task> response = new APIResponse<>();
        response.setMessage("Task successfully updated");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<APIResponse<Task>> deleteTask(@PathVariable Long id) {
        APIResponse<Task> response = new APIResponse<>();
        response.setMessage("Task successfully deleted");
        response.setData(null);
        return ResponseEntity.ok(response);
    }
}
