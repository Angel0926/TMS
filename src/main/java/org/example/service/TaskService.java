package org.example.service;

import org.example.handler.TaskNotFoundException;
import org.example.handler.UnauthorizedActionException;
import org.example.handler.UserNotFoundException;
import org.example.models.User;
import org.example.models.Task;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(Task task, String email) {
        User author = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        task.setAuthor(author);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task task, String email) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (existingTask.getAuthor().getEmail().equals(email) || // author can edit
                email.equals("admin@example.com")) { // admin can edit any task
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            existingTask.setPriority(task.getPriority());
            return taskRepository.save(existingTask);
        } else {
            throw new RuntimeException("You do not have permission to edit this task");
        }
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public List<Task> getTasksByAuthor(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return taskRepository.findByAuthor(user, pageable);
    }

    public List<Task> getTasksByAssignee(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return taskRepository.findByAssignee(user, pageable);
    }

    public List<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).getContent();
    }

    public void deleteTask(Long taskId, String email) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!task.getAuthor().getEmail().equals(email)) {
            throw new UnauthorizedActionException("Only the task author can delete the task");
        }

        taskRepository.delete(task);
    }

}
