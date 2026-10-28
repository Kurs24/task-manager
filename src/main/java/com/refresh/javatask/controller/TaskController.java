package com.refresh.javatask.controller;

import com.refresh.javatask.responsedto.APIResponse;
import com.refresh.javatask.entity.Task;
import com.refresh.javatask.requestdto.TaskRequest;
import com.refresh.javatask.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<APIResponse<Task>> createTask(@Valid @RequestBody TaskRequest request) {
        Task created = taskService.createTask(request);
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location)
                .body(APIResponse.of(created, "Task successfully created"));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Task>>> getTasks(@RequestParam(required = false) Task.Status status) {
        List<Task> taskLists = taskService.findTasks(status);
        return ResponseEntity.ok().body(APIResponse.of(taskLists, "Tasks successfully retrieved"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> getTaskById(@PathVariable Long id) {
        Task foundTask = taskService.findTaskById(id);
        return ResponseEntity.ok().body(APIResponse.of(foundTask, "Task successfully found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Task>> updateTask(@Valid @RequestBody TaskRequest request,
                                                        @PathVariable Long id) {
        Task updated = taskService.updateTask(request, id);
        return ResponseEntity.ok(APIResponse.of(updated, "Task successfully updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
