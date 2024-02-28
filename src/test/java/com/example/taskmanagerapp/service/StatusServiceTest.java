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

import com.example.taskmanagerapp.model.Status;
import com.example.taskmanagerapp.repository.StatusRepository;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {
    @Mock
    private StatusRepository mockStatusRepository;

    @InjectMocks
    private StatusService target;

    @Tag("Q4")
    @Test
    void findAllStatusTest() {
        List<Status> expected = new ArrayList<>(List.of(
            new Status(1L, "status1"),
            new Status(2L, "status2")
        ));
        doReturn(expected).when(this.mockStatusRepository).findAll();
        List<Status> actual = target.findAllStatus();
        assertThat(expected, is(actual));
    }
}
