import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.TaskManagementApplication;
import org.example.models.Task;
import org.example.models.User;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.example.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = TaskManagementApplication.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@example.com", "password123");
    }

    @Test
    void testGetTasksByAssignee_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(taskRepository.findByAssignee(testUser, Pageable.unpaged())).thenReturn(new ArrayList<Task>());

        List<Task> tasks = taskService.getTasksByAssignee(testUser.getEmail(), Pageable.unpaged());

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }
}
