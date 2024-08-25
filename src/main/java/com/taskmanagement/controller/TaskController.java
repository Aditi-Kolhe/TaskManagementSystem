package com.taskmanagement.controller;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        task.setUser(user);
        task.setCreatedAt(new Date());
        return taskRepository.save(task);
    }

//    @GetMapping
//    public List<Task> getTasks() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(username);
//        return taskRepository.findByUserId(user.getId());
//    }
    
    @GetMapping
    public List<Task> getTasks() {
        // Retrieve the username from the security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Check if the user exists in the repository
        User user = userRepository.findByUsername(username);

        // If user is found, retrieve tasks for that user
        if (user != null) {
            return taskRepository.findByUserId(user.getId());
        } else {
            // If user is null, return all tasks (including those with null users)
            return taskRepository.findAll();
        }
    }



    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable Long taskId, @RequestBody Task taskUpdates) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setTitle(taskUpdates.getTitle());
        task.setDescription(taskUpdates.getDescription());
        task.setStatus(taskUpdates.getStatus());
        task.setPriority(taskUpdates.getPriority());
        task.setUpdatedAt(new Date());
        return taskRepository.save(task);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskRepository.deleteById(taskId);
    }


    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/filter")
//    public ResponseEntity<List<Task>> getTasks(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String priority,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDateStart,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDateEnd,
//            @RequestParam(required = false) String searchTitle,
//            @RequestParam(required = false) String searchDescription
//    ) {
//        List<Task> tasks;
//        
//        // Handle search or filter based on request params
//        if (searchTitle != null || searchDescription != null) {
//            tasks = taskService.searchTasks(searchTitle, searchDescription);
//        } else {
//            tasks = taskService.filterTasks(status, priority, dueDateStart, dueDateEnd);
//        }
//        
//        return ResponseEntity.ok(tasks);
//    }

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> getFilteredTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDateEnd,
            @RequestParam(required = false) String searchTitle,
            @RequestParam(required = false) String searchDescription
    ) {
        List<Task> tasks = taskService.filterAndSearchTasks(status, priority, dueDateStart, dueDateEnd, searchTitle, searchDescription);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/paginated")
    public Page<Task> getTasks(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {
        return taskService.getTasks(page, size);
    }
    
}
