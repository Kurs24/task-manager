package com.refresh.javatask.service;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.exception.BusinessRuleException;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestdto.TaskRequest;
import com.refresh.javatask.responsedto.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-11-20T00:00:00Z"), ZoneOffset.UTC);
    private static final LocalDateTime NOW = LocalDateTime.now(FIXED_CLOCK);

    @Mock
    private TaskRepository taskRepository;

    private TaskServiceImpl taskService;

    @BeforeEach
    void beforeEach() {
        taskService = new TaskServiceImpl(taskRepository, FIXED_CLOCK);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("validDueDates")
    void createTask_validDueDate_savesAndReturnsResponse(String caseName, LocalDateTime dueDate) {
        // Given
        TaskRequest request = requestWithDueDate(dueDate);
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        // When
        TaskResponse response = taskService.createTask(request);

        // Then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Testing Task");
        assertThat(captor.getValue().getDescription()).isEqualTo("Testing Task in action");
        assertThat(captor.getValue().getStatus()).isEqualTo(Task.Status.TODO);
        assertThat(captor.getValue().getDueDate()).isEqualTo(dueDate);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Testing Task");
        assertThat(response.getDescription()).isEqualTo("Testing Task in action");
        assertThat(response.getStatus()).isEqualTo(Task.Status.TODO);
        assertThat(response.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    @DisplayName("createTask: due date in the past is rejected and nothing is saved")
    void createTask_dueDateInPast_throwsAndDoesNotSave() {
        //Given
        TaskRequest request = new TaskRequest();
        request.setTitle("Late task");
        request.setStatus(Task.Status.TODO);
        request.setDueDate(NOW.minusSeconds(1));

        //When
        assertThatThrownBy(() -> taskService.createTask(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Due date cannot be in the past");

        //Then
        verify(taskRepository, never()).save(any());
    }

    static Stream<Arguments> validDueDates() {
        return Stream.of(
                Arguments.of("due date in the future", NOW.plusDays(1)),
                Arguments.of("no due date (null)", null),
                Arguments.of("due date equals now (boundary)", NOW));
    }

    private static TaskRequest requestWithDueDate(LocalDateTime dueDate) {
        TaskRequest request = new TaskRequest();
        request.setTitle("Testing Task");
        request.setDescription("Testing Task in action");
        request.setStatus(Task.Status.TODO);
        request.setDueDate(dueDate);
        return request;
    }
}
