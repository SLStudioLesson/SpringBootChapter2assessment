package com.example.taskmanagerapp.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.taskmanagerapp.model.Label;
import com.example.taskmanagerapp.model.Status;
import com.example.taskmanagerapp.model.Task;
import com.example.taskmanagerapp.model.User;
import com.example.taskmanagerapp.service.LabelService;
import com.example.taskmanagerapp.service.StatusService;
import com.example.taskmanagerapp.service.TaskService;
import com.example.taskmanagerapp.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskService mockTaskService;

    @Mock
    private UserService mockUserService;

    @Mock
    private StatusService mockStatusService;

    @Mock
    private LabelService mockLabelService;

    @InjectMocks
    private TaskController target;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    }

    @Tag("Q3")
    @Test
    void listTasksTest() throws Exception {
        List<Task> expectedTasks = getTasksData();

        doReturn(expectedTasks).when(this.mockTaskService).findAllTasks();

        this.mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attribute("tasks", is(expectedTasks)));
    }

    @Tag("Q4")
    @Test
    void editTaskFormTest() throws Exception {
        Task expectedTask = new Task();
        expectedTask.setId(1L);
        expectedTask.setName("task1");
        expectedTask.setUser(new User(1L, "ichiro"));
        expectedTask.setStatus(new Status(1L, "status1"));

        List<User> expectedUsers = Arrays.asList(
            new User(1L, "ichiro"),
            new User(2L, "ziro")
        );

        List<Status> expectedStatusList = Arrays.asList(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        );

        doReturn(expectedTask).when(mockTaskService).findTaskById(1L);
        doReturn(expectedUsers).when(mockUserService).findAllUsers();
        doReturn(expectedStatusList).when(mockStatusService).findAllStatus();

        this.mockMvc.perform(get("/tasks/edit/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/edit"))
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("task", is(expectedTask)))
            .andExpect(model().attribute("users", is(expectedUsers)))
            .andExpect(model().attribute("statusList", is(expectedStatusList)));
    }

    @Tag("Q4")
    @Test
    void editTaskTest() throws Exception {
        mockMvc.perform(post("/tasks/edit")
            .param("id", "1")
            .param("name", "task1")
            .param("user.id", "1")
            .param("status.id", "1"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/tasks"));
    }

    @Tag("Q5")
    @Test
    void deleteTaskTest() throws Exception {
        mockMvc.perform(get("/tasks/delete/1"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/tasks"));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest() throws Exception {
        List<Task> expectedTasks = getTasksData();

        List<User> expectedUsers = Arrays.asList(
            new User(1L, "ichiro"),
            new User(2L, "ziro")
        );

        List<Status> expectedStatusList = Arrays.asList(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        );

        String name = "ichiro";
        Long statusId = 1L;
        Long userId = 1L;

        doReturn(expectedTasks).when(mockTaskService).findTaskByParams(name, statusId, userId);
        doReturn(expectedUsers).when(mockUserService).findAllUsers();
        doReturn(expectedStatusList).when(mockStatusService).findAllStatus();

        this.mockMvc.perform(get("/tasks/search")
            .param("name", name)
            .param("statusId", statusId.toString())
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", is(expectedTasks)))
            .andExpect(model().attribute("users", is(expectedUsers)))
            .andExpect(model().attribute("statusList", is(expectedStatusList)));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_noName() throws Exception {
        List<Task> expectedTasks = getTasksData();

        List<User> expectedUsers = Arrays.asList(
            new User(1L, "ichiro"),
            new User(2L, "ziro")
        );

        List<Status> expectedStatusList = Arrays.asList(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        );

        Long statusId = 1L;
        Long userId = 1L;

        doReturn(expectedTasks).when(mockTaskService).findTaskByParams(null, statusId, userId);
        doReturn(expectedUsers).when(mockUserService).findAllUsers();
        doReturn(expectedStatusList).when(mockStatusService).findAllStatus();

        this.mockMvc.perform(get("/tasks/search")
            .param("statusId", statusId.toString())
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", is(expectedTasks)))
            .andExpect(model().attribute("users", is(expectedUsers)))
            .andExpect(model().attribute("statusList", is(expectedStatusList)));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_noStatusId() throws Exception {
        List<Task> expectedTasks = getTasksData();

        List<User> expectedUsers = Arrays.asList(
            new User(1L, "ichiro"),
            new User(2L, "ziro")
        );

        List<Status> expectedStatusList = Arrays.asList(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        );

        String name = "ichiro";
        Long userId = 1L;

        doReturn(expectedTasks).when(mockTaskService).findTaskByParams(name, null, userId);
        doReturn(expectedUsers).when(mockUserService).findAllUsers();
        doReturn(expectedStatusList).when(mockStatusService).findAllStatus();

        this.mockMvc.perform(get("/tasks/search")
            .param("name", name)
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", is(expectedTasks)))
            .andExpect(model().attribute("users", is(expectedUsers)))
            .andExpect(model().attribute("statusList", is(expectedStatusList)));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_noUserId() throws Exception {
        List<Task> expectedTasks = getTasksData();

        List<User> expectedUsers = Arrays.asList(
            new User(1L, "ichiro"),
            new User(2L, "ziro")
        );

        List<Status> expectedStatusList = Arrays.asList(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        );

        String name = "ichiro";
        Long statusId = 1L;

        doReturn(expectedTasks).when(mockTaskService).findTaskByParams(name, statusId, null);
        doReturn(expectedUsers).when(mockUserService).findAllUsers();
        doReturn(expectedStatusList).when(mockStatusService).findAllStatus();

        this.mockMvc.perform(get("/tasks/search")
            .param("name", name)
            .param("statusId", statusId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", is(expectedTasks)))
            .andExpect(model().attribute("users", is(expectedUsers)))
            .andExpect(model().attribute("statusList", is(expectedStatusList)));
    }

    @Tag("Q7")
    @Test
    void addTaskForm() throws Exception {
        List<Label> expectedLabels = Arrays.asList(
            new Label(1L, "label1"),
            new Label(2L, "label2")
        );
        List<User> expectedUsers = Arrays.asList(
            new User(1L, "ichiro"),
            new User(2L, "ziro")
        );
        List<Status> expectedStatusList = Arrays.asList(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        );

        doReturn(expectedLabels).when(mockLabelService).findAllLabels();
        doReturn(expectedUsers).when(mockUserService).findAllUsers();
        doReturn(expectedStatusList).when(mockStatusService).findAllStatus();

        this.mockMvc.perform(get("/tasks/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/add"))
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attributeExists("labels"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("task", instanceOf(Task.class)))
            .andExpect(model().attribute("labels", is(expectedLabels)))
            .andExpect(model().attribute("users", is(expectedUsers)))
            .andExpect(model().attribute("statusList", is(expectedStatusList)));
    }

    @Tag("Q7")
    @Test
    void addTaskTest() throws Exception {
        Task task = new Task();
        task.setName("tasl1");
        task.setUser(new User(1L, "ichiro"));
        task.setStatus(new Status(1L, "status1"));
        List<Long> labelIds = Arrays.asList(1L, 2L);
        Set<Label> labels = labelIds.stream()
            .map(labelId -> new Label(1L, "label" + labelId.toString()))
            .collect(Collectors.toSet());
        task.setLabels(labels);
        mockMvc.perform(post("/tasks/add")
            .flashAttr("task", task))
            .andExpect(status().is(400));
    }

    private List<Task> getTasksData() {
        List<Task> expectedTasks = new ArrayList<>();
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

        expectedTasks.add(task1);
        expectedTasks.add(task2);

        return expectedTasks;
    }
}
