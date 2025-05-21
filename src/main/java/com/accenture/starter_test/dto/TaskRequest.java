package com.accenture.starter_test.dto;

import com.accenture.starter_test.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskRequest(@NotBlank(message = "Title is required") String title,

                          String description,

                          @NotNull(message = "Status is required") Task.TaskStatus status,

                          LocalDateTime dueDate) {


}

