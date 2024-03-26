package com.example.taskmanagerapp;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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

import com.example.taskmanagerapp.model.Task;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class Q4Test {
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

    @Tag("Q4")
    @Test
    void editTaskFormTest() throws Exception {
        TestDBUtil.insertTasks(em, 1, 2);
        this.mockMvc.perform(get("/tasks/edit/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/edit"))
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("task", allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("task1")),
                hasProperty("user", allOf(
                    hasProperty("id", is(1L)),
                    hasProperty("name", is("user1"))
                )),
                hasProperty("status", allOf(
                    hasProperty("id", is(1L)),
                    hasProperty("name", is("status1"))
                ))
            )))
            .andExpect(model().attribute("users", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("user1"))
            ))))
            .andExpect(model().attribute("users", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("user2"))
            ))))
            .andExpect(model().attribute("statusList", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("status1"))
            ))))
            .andExpect(model().attribute("statusList", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("status2"))
            ))));
    }

    @Tag("Q4")
    @Test
    void editTaskTest() throws Exception {
        TestDBUtil.insertTasks(em, 1, 2);
        mockMvc.perform(post("/tasks/edit")
            .param("id", "1")
            .param("name", "task1Edit")
            .param("user.id", "2")
            .param("status.id", "2"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/tasks"));

        Task expected = em.find(Task.class, 1);
        assertEquals(1L, expected.getId());
        assertEquals("task1Edit", expected.getName());
        assertEquals(2L, expected.getUser().getId());
        assertEquals(2L, expected.getStatus().getId());
    }
}
