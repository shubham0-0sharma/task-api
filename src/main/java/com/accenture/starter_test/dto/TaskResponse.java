package com.accenture.starter_test.dto;

import com.accenture.starter_test.entity.Task;

import java.time.LocalDateTime;

public record TaskResponse(Long id, String title, String description, Task.TaskStatus status,
                           LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime dueDate) {
}
