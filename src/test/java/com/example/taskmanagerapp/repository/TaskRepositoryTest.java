package com.example.taskmanagerapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.taskmanagerapp.model.Status;
import com.example.taskmanagerapp.model.Task;
import com.example.taskmanagerapp.model.User;

@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() throws Exception {
        Status status1 = new Status();
        status1.setName("status1");
        Status status2 = new Status();
        status2.setName("status2");
        Status status3 = new Status();
        status3.setName("status3");

        User user1 = new User();
        user1.setName("ichiro");
        User user2 = new User();
        user2.setName("ziro");
        User user3 = new User();
        user3.setName("saburo");

        Task task1 = new Task();
        task1.setName("task1");
        task1.setUser(user1);
        task1.setStatus(status1);

        Task task2 = new Task();
        task2.setName("task2");
        task2.setUser(user2);
        task2.setStatus(status2);

        Task task3 = new Task();
        task3.setName("task3");
        task3.setUser(user3);
        task3.setStatus(status3);

        List<Task> taskDataList = new ArrayList<>();

        taskDataList.add(task1);
        taskDataList.add(task2);
        taskDataList.add(task3);

        for (Task task : taskDataList) {
            em.persist(task.getStatus());
            em.persist(task.getUser());
            em.persist(task);
        }
    }

    @AfterEach
    void setDown() throws Exception {
        em.getEntityManager().createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE task_label RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE labels RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE tasks RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("TRUNCATE TABLE status RESTART IDENTITY").executeUpdate();
        em.getEntityManager().createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_onlyName() {
        List<Task> expected = Arrays.asList(
            new Task(1L, "task1")
        );
        List<Task> actual = taskRepository.findByParams("task1", null, null);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_onlyStatusId() {
        List<Task> expected = Arrays.asList(
            new Task(2L, "task2")
        );
        List<Task> actual = taskRepository.findByParams(null, 2L, null);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_onlyUserId() {
        List<Task> expected = Arrays.asList(
            new Task(3L, "task3")
        );
        List<Task> actual = taskRepository.findByParams(null, null, 3L);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_nameAndStatusId() {
        List<Task> expected = Arrays.asList(
            new Task(1L, "task1"),
            new Task(2L, "task2")
        );
        List<Task> actual = taskRepository.findByParams("task1", 2L, null);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_nameAndUserId() {
        List<Task> expected = Arrays.asList(
            new Task(1L, "task1"),
            new Task(3L, "task3")
        );
        List<Task> actual = taskRepository.findByParams("task1", null, 3L);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_statusIdAndUserId() {
        List<Task> expected = Arrays.asList(
            new Task(2L, "task2"),
            new Task(3L, "task3")
        );
        List<Task> actual = taskRepository.findByParams(null, 2L, 3L);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }

    @Tag("Q6")
    @Test
    void findByParamsTest_all() {
        List<Task> expected = Arrays.asList(
            new Task(1L, "task1"),
            new Task(2L, "task2"),
            new Task(3L, "task3")
        );
        List<Task> actual = taskRepository.findByParams("task1", 2L, 3L);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId());
            assertEquals(expected.get(i).getName(), actual.get(i).getName());
        }
    }
}
