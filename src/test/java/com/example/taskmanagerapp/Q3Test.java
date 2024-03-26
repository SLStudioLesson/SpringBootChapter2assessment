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
public class Q3Test {
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

    @Tag("Q3")
    @Test
    void listTasksTest() throws Exception {
        TestDBUtil.insertTasks(em, 1, 2);
        this.mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/list"))
            .andExpect(model().attributeExists("tasks"))
            .andExpect(model().attribute("tasks", hasItem(allOf(
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
            ))))
            .andExpect(model().attribute("tasks", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("task2")),
                hasProperty("user", allOf(
                    hasProperty("id", is(2L)),
                    hasProperty("name", is("user2"))
                )),
                hasProperty("status", allOf(
                    hasProperty("id", is(2L)),
                    hasProperty("name", is("status2"))
                ))
            ))));
    }
}
