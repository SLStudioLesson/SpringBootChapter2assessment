package com.example.taskmanagerapp.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.taskmanagerapp.model.Label;
import com.example.taskmanagerapp.model.Status;
import com.example.taskmanagerapp.model.Task;
import com.example.taskmanagerapp.model.User;
import com.example.taskmanagerapp.repository.TaskRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository mockTaskRepository;

    @InjectMocks
    private TaskService target;

    @Tag("Q3")
    @Test
    void findAllTasksTest() {
        List<Task> expected = getTasksData();
        doReturn(expected).when(this.mockTaskRepository).findAll();
        List<Task> actual = target.findAllTasks();
        assertThat(expected, is(actual));
    }

    @Tag("Q4")
    @Test
    void findTaskByIdTest() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setName("task1");
        task.setUser(new User(1L, "ichiro"));
        task.setStatus(new Status(1L, "status1"));

        Optional<Task> expected = Optional.of(task);

        Long id = 1L;

        doReturn(expected).when(this.mockTaskRepository).findById(id);
        Task actual = target.findTaskById(id);
        assertThat(expected.get(), is(actual));
    }

    @Tag("Q4")
    @Test
    void findTaskById_throwException() throws Exception {
        Optional<Task> expected = Optional.ofNullable(null);

        Long id = 500L;
        doReturn(expected).when(this.mockTaskRepository).findById(id);
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            target.findTaskById(id);
        });
        assertEquals("Task not found with id: " + id, exception.getMessage());
    }

    @Tag("Q4")
    @Test
    void updateTaskTest() throws Exception {
        Task expected = new Task();
        expected.setId(1L);
        expected.setName("task1");
        expected.setUser(new User(1L, "ichiro"));
        expected.setStatus(new Status(1L, "status1"));

        doReturn(expected).when(this.mockTaskRepository).save(expected);
        Task actual = target.updateTask(expected);
        assertThat(expected, is(actual));
    }

    @Tag("Q5")
    @Test
    void deleteTaskTest() throws Exception {
        Task expected = new Task();
        expected.setId(1L);
        expected.setName("task1");
        expected.setUser(new User(1L, "ichiro"));
        expected.setStatus(new Status(1L, "status1"));
        expected.setDeleted(true);

        doReturn(expected).when(this.mockTaskRepository).save(expected);
        Task actual = target.deleteTask(expected);
        assertThat(expected, is(actual));
    }

    @Tag("Q6")
    @Test
    void findTaskByParamsTest() throws Exception {
        List<Task> expected = getTasksData();
        String name = "ichiro";
        Long statusId = 1L;
        Long userId = 1L;
        doReturn(expected).when(this.mockTaskRepository).findByParams(name, statusId, userId);
        List<Task> actual = target.findTaskByParams(name, statusId, userId);
        assertThat(expected, is(actual));
    }

    @Tag("Q6")
    @Test
    void findTaskByParamsTest_paramsNull() throws Exception {
        List<Task> expected = getTasksData();
        doReturn(expected).when(this.mockTaskRepository).findAll();
        List<Task> actual = target.findTaskByParams(null, null, null);
        assertThat(expected, is(actual));
    }

    @Tag("Q7")
    @Test
    void addTaskTest() throws Exception {
        Task expected = new Task();
        expected.setId(1L);
        expected.setName("task1");
        expected.setUser(new User(1L, "ichiro"));
        expected.setStatus(new Status(1L, "status1"));
        expected.setDeleted(true);
        Set<Label> labels = new HashSet<>(Arrays.asList(
            new Label(1L, "label1"),
            new Label(2L, "label2")
        ));
        doReturn(expected).when(this.mockTaskRepository).save(expected);
        Task actual = target.addTask(expected, labels);
        assertThat(expected, is(actual));
    }

    private List<Task> getTasksData() {
        List<Task> expected = new ArrayList<>();
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("task1");
        task1.setUser(new User(1L, "ichiro"));
        task1.setStatus(new Status(1L, "status1"));

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("task2");
        task2.setUser(new User(2L, "zirou"));
        task2.setStatus(new Status(2L, "status2"));

        expected.add(task1);
        expected.add(task2);

        return expected;
    }
}
