package com.example.taskmanagerapp.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.taskmanagerapp.model.User;
import com.example.taskmanagerapp.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserController target;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    }

    @Tag("Q1")
    @Test
    void listUsersTest() throws Exception {
        List<User> expectedUsers = new ArrayList<>(List.of(
            new User(1L, "ishikawa"),
            new User(2L, "soda")
        ));

        doReturn(expectedUsers).when(this.mockUserService).findAllUsers();

        this.mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(view().name("users/list"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("users", is(expectedUsers)));
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
    }
}
