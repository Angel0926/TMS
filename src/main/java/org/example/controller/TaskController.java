package org.example.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.models.Task;
import org.example.service.TaskService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Task createdTask = taskService.createTask(task, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskDetails, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Task updatedTask = taskService.updateTask(taskId, taskDetails, email);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        taskService.deleteTask(taskId, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Task>> getTasksByAuthor(@RequestHeader("Authorization") String token, @RequestParam int page, @RequestParam int size) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskService.getTasksByAuthor(email, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assignee")
    public ResponseEntity<List<Task>> getTasksByAssignee(@RequestHeader("Authorization") String token, @RequestParam int page, @RequestParam int size) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskService.getTasksByAssignee(email, pageable);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Task> tasks = taskService.getAllTasks(pageable);
        return ResponseEntity.ok(tasks);
    }
}
