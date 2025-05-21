package com.accenture.starter_test.controller;

import com.accenture.starter_test.dto.TaskRequest;
import com.accenture.starter_test.dto.TaskResponse;
import com.accenture.starter_test.entity.Task;
import com.accenture.starter_test.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;  // Used to simulate HTTP requests
    @MockitoBean
    private TaskService taskService;  // Mocking the service layer

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTasks_ShouldReturnListOfTasks_WhenTaskExist() throws Exception {

//            arrange
        List<TaskResponse> taskResponses =
                asList(new TaskResponse(1L, "Task 1", "Description 1", Task.TaskStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1)),
                        new TaskResponse(2L, "Task 2", "Description 2", Task.TaskStatus.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(2)));
//        act
        when(taskService.getAllTasks()).thenReturn(taskResponses);
//        perform the request and assert the response
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].title").value("Task 1"))
                .andExpect(jsonPath("[0].description").value("Description 1"));


    }

    @Test
    void getAllTasks_ShouldReturnEmptyList_WhenNoTasksExist() throws Exception {
//        arrange
        List<TaskResponse> taskResponses = asList();
//        act
        when(taskService.getAllTasks()).thenReturn(taskResponses);
//        perform the request and assert the response
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
//        arrange
        TaskResponse taskResponse = new TaskResponse(1L, "Task 1", "Description 1", Task.TaskStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        when(taskService.getTaskById(1L)).thenReturn(taskResponse);
//        act
        mockMvc.perform(get("/api/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));
    }

    @Test
    void getTaskById_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(1L)).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    void createTask_ShouldReturnCreatedTask_WhenRequestIsValid() throws Exception {
        TaskRequest taskRequest = new TaskRequest("Task 1", "Description 1", Task.TaskStatus.PENDING, LocalDateTime.now().plusDays(1));
        TaskResponse taskResponse = new TaskResponse(1L, "Task 1", "Description 1", Task.TaskStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        when(taskService.createTask(any(TaskRequest.class))).thenReturn(taskResponse);


        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        TaskRequest invalidRequest = new TaskRequest("", "", null, null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenRequestIsValid() throws Exception {
        TaskRequest taskRequest = new TaskRequest("Updated Task", "Updated Description", Task.TaskStatus.IN_PROGRESS, LocalDateTime.now().plusDays(2));
        TaskResponse taskResponse = new TaskResponse(1L, "Updated Task", "Updated Description", Task.TaskStatus.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(2));

        Mockito.when(taskService.updateTask(eq(1L), any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void updateTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        TaskRequest taskRequest = new TaskRequest("Updated Task", "Updated Description", Task.TaskStatus.IN_PROGRESS, LocalDateTime.now().plusDays(2));

        Mockito.when(taskService.updateTask(eq(1L), any(TaskRequest.class))).thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    void deleteTask_ShouldReturnNoContent_WhenTaskIsDeleted() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        Mockito.doThrow(new RuntimeException("Task not found")).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task not found"));
    }

    @Test
    void getTasksByStatus_ShouldReturnTasks_WhenTasksWithStatusExist() throws Exception {
        List<TaskResponse> tasks = Arrays.asList(
                new TaskResponse(1L, "Task 1", "Description 1", Task.TaskStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );

        Mockito.when(taskService.getTasksByStatus(Task.TaskStatus.PENDING)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void searchTasksByTitle_ShouldReturnTasks_WhenMatchingTasksExist() throws Exception {
        List<TaskResponse> tasks = Arrays.asList(
                new TaskResponse(1L, "Task 1", "Description 1", Task.TaskStatus.PENDING, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );

        Mockito.when(taskService.searchTasksByTitle("Task")).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/search?title=Task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }


}