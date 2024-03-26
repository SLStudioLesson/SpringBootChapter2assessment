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
public class Q1Test {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        TestDBUtil.resetDB(em);
        TestDBUtil.insertUsers(em, 1, 2);
    }

    @AfterEach
    void setDown() throws Exception {
        TestDBUtil.resetDB(em);
    }

    @Tag("Q1")
    @Test
    void listUsersTest() throws Exception {
        this.mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/list"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("users", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("user1"))
            ))))
            .andExpect(model().attribute("users", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("user2"))
            ))));
    }
}
