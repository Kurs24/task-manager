package com.refresh.javatask.controller.Rest;

import com.refresh.javatask.entity.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
