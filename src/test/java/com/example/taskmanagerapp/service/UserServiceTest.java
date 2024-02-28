package com.example.taskmanagerapp.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.taskmanagerapp.model.User;
import com.example.taskmanagerapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private UserService target;

    @Tag("Q1")
    @Test
    void findAllUsersTest() {
        List<User> expected = new ArrayList<>(List.of(
            new User(1L, "ishikawa"),
            new User(2L, "soda")
        ));
        doReturn(expected).when(this.mockUserRepository).findAll();
        List<User> actual = target.findAllUsers();
        assertThat(expected, is(actual));
    }

    @Tag("Q2")
    @Test
    void saveUserTest() {
        User expected = new User();
        expected.setName("ichiro");
        doReturn(expected).when(this.mockUserRepository).save(expected);
        User actual = target.saveUser(expected);
        assertThat(expected, is(actual));
    }
}
