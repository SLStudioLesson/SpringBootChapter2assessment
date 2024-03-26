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

import com.example.taskmanagerapp.model.User;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class Q2Test {
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

    @Tag("Q2")
    @Test
    void addUserFormTest() throws Exception {
        this.mockMvc.perform(get("/users/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/add"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", instanceOf(User.class)));
    }

    @Tag("Q2")
    @Test
    void addUserTest() throws Exception {
        mockMvc.perform(post("/users/add")
            .param("name", "test"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/users"));

        int size = em.getEntityManager().createQuery("from User u").getResultList().size();
        User expected = em.find(User.class, size);
        assertEquals(3L, expected.getId());
        assertEquals("test", expected.getName());
    }
}
