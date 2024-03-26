package com.example.taskmanagerapp;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

import com.example.taskmanagerapp.model.Label;
import com.example.taskmanagerapp.model.Task;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
public class Q7Test {
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

    @Tag("Q7")
    @Test
    void addTaskForm() throws Exception {
        TestDBUtil.insertTasks(em, 1, 2);
        TestDBUtil.insertLabels(em, 1, 2);

        this.mockMvc.perform(get("/tasks/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("tasks/add"))
            .andExpect(model().attributeExists("task"))
            .andExpect(model().attributeExists("labels"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attributeExists("statusList"))
            .andExpect(model().attribute("task", instanceOf(Task.class)))
            .andExpect(model().attribute("labels", hasItem(allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is("label1"))
            ))))
            .andExpect(model().attribute("labels", hasItem(allOf(
                hasProperty("id", is(2L)),
                hasProperty("name", is("label2"))
            ))))
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

    @Tag("Q7")
    @Test
    void addTaskTest() throws Exception {
        TestDBUtil.insertTasks(em, 1, 2);
        TestDBUtil.insertLabels(em, 1, 2);

        mockMvc.perform(post("/tasks/add")
            .param("name", "task3")
            .param("user.id", "1")
            .param("status.id", "1")
            .param("labels", "1,2"))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("/tasks"));

        int size = em.getEntityManager().createQuery("from Task t").getResultList().size();
        Task expected = em.find(Task.class, size);
        assertEquals(3L, expected.getId());
        assertEquals("task3", expected.getName());
        assertEquals(1L, expected.getUser().getId());
        assertEquals(1L, expected.getStatus().getId());

        List<Label> sortedLabels = expected.getLabels().stream()
            .sorted(Comparator.comparing(Label::getId, Comparator.naturalOrder()))
            .collect(Collectors.toList());

        Long i = 1L;
        assertEquals(2, sortedLabels.size());
        for (Label label : sortedLabels) {
            assertEquals(i, label.getId());
            i++;
        }
    }
}
