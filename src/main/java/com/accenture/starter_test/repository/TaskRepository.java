package com.accenture.starter_test.repository;

import com.accenture.starter_test.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long>
{
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByTitleContainingIgnoreCase(String title);
}
