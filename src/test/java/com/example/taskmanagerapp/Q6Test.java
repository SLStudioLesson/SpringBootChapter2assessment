package com.example.taskmanagerapp;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class Q6Test {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        TestDBUtil.resetDB(em);
    }

    @AfterEach
    void setDown() throws Exception {
        TestDBUtil.resetDB(em);
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_onlyStatusId() throws Exception {
        TestDBUtil.insertTasks(em, 1, 5);
        Long statusId = 2L;

        this.mockMvc.perform(get("/tasks/search")
            .param("statusId", statusId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("task2"))
            ))));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_onlyUserId() throws Exception {
        TestDBUtil.insertTasks(em, 1, 5);
        Long userId = 3L;

        this.mockMvc.perform(get("/tasks/search")
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(3L)),
                hasProperty("name", is("task3"))
            ))));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_nameAndStatusId() throws Exception {
        TestDBUtil.insertTasks(em, 1, 5);
        String name = "task1";
        Long statusId = 2L;

        this.mockMvc.perform(get("/tasks/search")
            .param("name", name)
            .param("statusId", statusId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("task1"))
            ))))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("task2"))
            ))));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_nameAndUserId() throws Exception {
        TestDBUtil.insertTasks(em, 1, 5);
        String name = "task1";
        Long userId = 3L;

        this.mockMvc.perform(get("/tasks/search")
            .param("name", name)
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("task1"))
            ))))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(3L)),
                hasProperty("name", is("task3"))
            ))));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_statusIdAndUserId() throws Exception {
        TestDBUtil.insertTasks(em, 1, 5);
        Long statusId = 2L;
        Long userId = 3L;

        this.mockMvc.perform(get("/tasks/search")
            .param("statusId", statusId.toString())
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("task2"))
            ))))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(3L)),
                hasProperty("name", is("task3"))
            ))));
    }

    @Tag("Q6")
    @Test
    void searchTaskTest_allParams() throws Exception {
        TestDBUtil.insertTasks(em, 1, 5);

        String name = "task1";
        Long statusId = 2L;
        Long userId = 3L;

        this.mockMvc.perform(get("/tasks/search")
            .param("name", name)
            .param("statusId", statusId.toString())
            .param("userId", userId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/search"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("task1"))
            ))))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("task2"))
            ))))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(3L)),
                hasProperty("name", is("task3"))
            ))));
    }
}
