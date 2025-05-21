package com.accenture.starter_test.service;

import com.accenture.starter_test.dto.TaskRequest;
import com.accenture.starter_test.dto.TaskResponse;
import com.accenture.starter_test.entity.Task;
import com.accenture.starter_test.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;


    @Test
    void gGetAllTasksTest() {
        // Arrange: Prepare fake task entities
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(Task.TaskStatus.PENDING);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setUpdatedAt(LocalDateTime.now());
        task1.setDueDate(LocalDateTime.now().plusDays(1));

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus(Task.TaskStatus.IN_PROGRESS);
        task2.setCreatedAt(LocalDateTime.now());
        task2.setUpdatedAt(LocalDateTime.now());
        task2.setDueDate(LocalDateTime.now().plusDays(2));

        List<Task> fakeTasks = Arrays.asList(task1, task2);

        // Mock repository response
        when(taskRepository.findAll()).thenReturn(fakeTasks);

        // Act
        List<TaskResponse> result = taskService.getAllTasks();
        System.out.println(result);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).title());
        assertEquals("Task 2", result.get(1).title());

        // Verify repository interaction
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void mapToDtoTest() {
        // Arrange: Prepare data
        Task task = Task.builder()
                .id(1L)
                .description("test")
                .status(Task.TaskStatus.PENDING)
                .title("myTask")
                .dueDate(LocalDateTime.now().plusDays(2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        // Act: Call the mapToDto() method to convert the entity to a DTO (TaskResponse)
        TaskResponse taskResponse = taskService.mapToDto(task);
        // Assert: Validate that the mapped DTO fields match the original Task entity fields
        assertEquals(task.getId(), taskResponse.id());
        assertEquals(task.getTitle(), taskResponse.title());
        assertEquals(task.getDescription(), taskResponse.description());
        assertEquals(task.getStatus(), taskResponse.status());
        assertEquals(task.getCreatedAt(), taskResponse.createdAt());
        assertEquals(task.getUpdatedAt(), taskResponse.updatedAt());
        assertEquals(task.getDueDate(), taskResponse.dueDate());


    }

    @Test
    public void mapToDto_Null_ExceptionTest() {
        // Arrange: Prepare data
        Task task = null;
        // Act: Call the mapToDto() method to convert the entity to a DTO (TaskResponse)'

        var msg = assertThrows(NullPointerException.class, () -> taskService.mapToDto(task));
        // Assert: Validate that the mapped DTO fields match the original Task entity fields
        assertEquals("Task not found", msg.getMessage());

    }

    @Test
    public void mapToEntityTest() {
//        arrange:
        TaskRequest dto = new TaskRequest("test", "test desc", Task.TaskStatus.PENDING, LocalDateTime.now().plusDays(3));
//        act
        Task task = Task.builder()
                .title(dto.title())
                .status(dto.status())
                .description(dto.description())
                .dueDate(dto.dueDate())
                .build();
        // Act: Map TaskRequest DTO to Task entity using builder


//    assert
        assertEquals(dto.title(), task.getTitle()); // Check if title is the same
        assertEquals(dto.status(), task.getStatus()); // Check if status is the same
        assertEquals(dto.description(), task.getDescription()); // Check if description is the same
        assertEquals(dto.dueDate(), task.getDueDate()); // Check if dueDate is the same
    }
}