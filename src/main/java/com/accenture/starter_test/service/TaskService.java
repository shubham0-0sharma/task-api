package com.accenture.starter_test.service;

import com.accenture.starter_test.dto.TaskRequest;
import com.accenture.starter_test.dto.TaskResponse;
import com.accenture.starter_test.entity.Task;
import com.accenture.starter_test.exception.UserNotFoundException;
import com.accenture.starter_test.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    //    convert entity to DTO
    public TaskResponse mapToDto(Task task) {
        if (task == null) {
            throw new NullPointerException("Task not found");
        }
        return new TaskResponse(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate());
    }

    // Convert DTO to Entity
    public Task mapToEntity(TaskRequest dto) {
        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setDueDate(dto.dueDate());
        return task;
    }

    @Transactional
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Task not found with id: " + id));
        return mapToDto(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = mapToEntity(taskRequest);
        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        // Update properties
        existingTask.setTitle(taskRequest.title());
        existingTask.setDescription(taskRequest.description());
        existingTask.setStatus(taskRequest.status());
        existingTask.setDueDate(taskRequest.dueDate());

        Task updatedTask = taskRepository.save(existingTask);
        return mapToDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public List<TaskResponse> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public List<TaskResponse> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title).stream().map(this::mapToDto).collect(Collectors.toList());
    }


}
