package com.refresh.javatask.service;

import com.refresh.javatask.entity.Task;
import com.refresh.javatask.exception.BusinessRuleException;
import com.refresh.javatask.exception.ResourceNotFoundException;
import com.refresh.javatask.mapper.TaskMapper;
import com.refresh.javatask.repository.TaskRepository;
import com.refresh.javatask.requestdto.TaskRequest;
import com.refresh.javatask.responsedto.TaskResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
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
        assertThatThrownBy(() -> taskService.createTask(request)).isInstanceOf(BusinessRuleException.class)
                .hasMessage("Due date cannot be in the past");

        //Then
        verify(taskRepository, never()).save(any());
    }

    @Nested
    @DisplayName("findTasks")
    class FindTasks {

        @ParameterizedTest(name = "status {0}: passed to repository, results mapped")
        @EnumSource(Task.Status.class)
        void givenStatus_delegatesToFindByStatus_andMapsResult(Task.Status status) {
            // Given
            Task task = taskFactory(1L, "Task 1", status, NOW.plusDays(1), "desc 1");
            when(taskRepository.findByStatus(status)).thenReturn(List.of(task));

            // When
            List<TaskResponse> response = taskService.findTasks(status);

            // Then
            assertThat(response).extracting(TaskResponse::getId, TaskResponse::getTitle, TaskResponse::getStatus,
                            TaskResponse::getDueDate, TaskResponse::getDescription)
                    .containsExactly(Tuple.tuple(1L, "Task 1", status, NOW.plusDays(1), "desc 1"));
            verify(taskRepository).findByStatus(status);
            verifyNoMoreInteractions(taskRepository);
        }

        @Test
        @DisplayName("no tasks found for status: returns empty list")
        void givenStatus_noTasksFound_returnsEmptyList() {
            //Given
            when(taskRepository.findByStatus(Task.Status.TODO)).thenReturn(List.of());

            //Then
            assertThat(taskService.findTasks(Task.Status.TODO)).isEmpty();
            verifyNoMoreInteractions(taskRepository);
        }

        @Test
        @DisplayName("null status: returns all tasks in repository order")
        void nullStatus_returnsAllTasks() {
            //Given
            Task first = taskFactory(1L, "Task 1", Task.Status.TODO, NOW.plusDays(1), "desc 1");
            Task second = taskFactory(2L, "Task 2", Task.Status.DONE, NOW.plusHours(1), "desc 2");
            when(taskRepository.findAll()).thenReturn(List.of(first, second));

            //When
            List<TaskResponse> response = taskService.findTasks(null);

            //Then
            assertThat(response).extracting(TaskResponse::getId).containsExactly(1L, 2L);
            verifyNoMoreInteractions(taskRepository);
        }
    }

    @Test
    @DisplayName("findTaskById: id found, task returned")
    void findTaskById_idFound_taskReturned() {
        //Given
        Task taskFound = taskFactory(1L, "task 1", Task.Status.IN_PROGRESS, NOW.plusDays(1), "desc 1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskFound));

        //When
        TaskResponse response = taskService.findTaskById(1L);

        //Then
        verify(taskRepository).findById(1L);
        assertThat(response).isEqualTo(TaskMapper.toTaskResponse(taskFound));
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(Task.Status.IN_PROGRESS);
        assertThat(response.getTitle()).isEqualTo("task 1");
        assertThat(response.getDescription()).isEqualTo("desc 1");
        assertThat(response.getDueDate()).isEqualTo(NOW.plusDays(1));
    }

    @Test
    @DisplayName("findTaskBydId: id not found, throw error")
    void findTaskById_idNotFound_throwError() {
        //Given
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        //When
        assertThatThrownBy(() -> taskService.findTaskById(1L)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Task with id 1 not found");
        verify(taskRepository).findById(1L);
        verifyNoMoreInteractions(taskRepository);
    }

    @Nested
    @DisplayName("updateTask")
    class UpdateTask {

        @Test
        @DisplayName("updateTask: Id Not Found, should return Not Found Exception")
        void updateTask_idNotFound_shouldThrowException() {
            //Given
            TaskRequest taskRequest = requestWithDueDate(NOW.plusDays(1));
            when(taskRepository.findById(1L)).thenReturn(Optional.empty());

            //When
            assertThatThrownBy(() -> taskService.updateTask(taskRequest, 1L)).isInstanceOf(
                    ResourceNotFoundException.class).hasMessage("Task with id 1 not found");
            verify(taskRepository).findById(1L);
            verifyNoMoreInteractions(taskRepository);
        }

        static Stream<Arguments> validUpdates() {
            return Stream.of(
                    // caseName, oldDueDate, oldDesc, newDueDate, newDesc
                    Arguments.of("all fields replaced",
                            NOW.plusDays(2), "old desc", NOW.plusDays(1), "new desc"),
                    Arguments.of("optional fields not sent are cleared (PUT = full replace)",
                            NOW.plusDays(2), "old desc", null, null),
                    Arguments.of("optional fields set on a task that had none",
                            null, null, NOW.plusDays(1), "new desc"),
                    Arguments.of("unchanged past due date is allowed",
                            NOW.minusDays(1), "old desc", NOW.minusDays(1), "new desc"),
                    Arguments.of("due date changed to exactly now (boundary)",
                            NOW.plusDays(2), "old desc", NOW, "new desc"));
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("validUpdates")
        void updateTask_validRequest_replacesAllFields(String caseName,
                                                       LocalDateTime oldDueDate, String oldDesc,
                                                       LocalDateTime newDueDate, String newDesc) {
            // Given
            Task existing = taskFactory(1L, "old title", Task.Status.IN_PROGRESS, oldDueDate, oldDesc);
            TaskRequest request = requestWithDueDate(newDueDate);
            request.setTitle("new title");
            request.setStatus(Task.Status.DONE);
            request.setDescription(newDesc);
            when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(taskRepository.saveAndFlush(existing)).thenAnswer(inv -> inv.getArgument(0));

            // When
            TaskResponse response = taskService.updateTask(request, 1L);

            // Then
            assertThat(response)
                    .extracting(TaskResponse::getId, TaskResponse::getTitle, TaskResponse::getStatus,
                            TaskResponse::getDueDate, TaskResponse::getDescription)
                    .containsExactly(1L, "new title", Task.Status.DONE, newDueDate, newDesc);
            verify(taskRepository).findById(1L);
            verify(taskRepository).saveAndFlush(existing);
            verifyNoMoreInteractions(taskRepository);
        }

        @Test
        @DisplayName("updateTask: due date is in the past")
        void updateTask_dueDateNotValid_throwError() {
            //Given
            Task taskFound = taskFactory(1L, "task 1", Task.Status.IN_PROGRESS, NOW.plusDays(1), "desc 1");
            TaskRequest taskRequest = requestWithDueDate(NOW.minusDays(1));
            when(taskRepository.findById(1L)).thenReturn(Optional.of(taskFound));

            //When
            assertThatThrownBy(() -> taskService.updateTask(taskRequest, 1L)).isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Due date cannot be in the past");
            verify(taskRepository).findById(1L);
            verifyNoMoreInteractions(taskRepository);
            assertThat(taskFound.getTitle()).isEqualTo("task 1");
        }
    }

    static Stream<Arguments> validDueDates() {
        return Stream.of(Arguments.of("due date in the future", NOW.plusDays(1)),
                Arguments.of("no due date (null)", null), Arguments.of("due date equals now (boundary)", NOW));
    }

    private static TaskRequest requestWithDueDate(LocalDateTime dueDate) {
        TaskRequest request = new TaskRequest();
        request.setTitle("Testing Task");
        request.setDescription("Testing Task in action");
        request.setStatus(Task.Status.TODO);
        request.setDueDate(dueDate);
        return request;
    }

    private static Task taskFactory(Long id, String title, Task.Status status, LocalDateTime dueDate, String desc) {
        return Task.builder().id(id).title(title).status(status).createdAt(NOW.minusDays(1))
                .updatedAt(NOW.minusHours(2)).dueDate(dueDate).description(desc).build();
    }
}
