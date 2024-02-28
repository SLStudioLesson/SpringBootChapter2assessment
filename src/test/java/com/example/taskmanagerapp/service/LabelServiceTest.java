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

import com.example.taskmanagerapp.model.Label;
import com.example.taskmanagerapp.repository.LabelRepository;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {
    @Mock
    private LabelRepository mockLabelRepository;

    @InjectMocks
    private LabelService target;

    @Tag("Q7")
    @Test
    void findAllLabelsTest() {
        List<Label> expected = new ArrayList<>(List.of(
            new Label(1L, "label1"),
            new Label(2L, "label2")
        ));
        doReturn(expected).when(this.mockLabelRepository).findAll();
        List<Label> actual = target.findAllLabels();
        assertThat(expected, is(actual));
    }
}
