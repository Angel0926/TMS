import org.example.TaskManagementApplication;
import org.example.models.Task;
import org.example.models.User;
import org.example.repository.TaskRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TaskManagementApplication.class)
class TaskRepositoryTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskRepositoryTest taskRepositoryTest;

    @Test
    void testFindByAssignee() {
        User user = new User("test@example.com", "password123");

        when(userRepository.save(user)).thenReturn(user);

        Task task = new Task("Тестовое задание", "Описание теста", user);

        when(taskRepository.findByAssignee(user)).thenReturn(Optional.of(task));

        Optional<Task> tasks = taskRepository.findByAssignee(user);

        assertNotNull(tasks);
        assertTrue(tasks.isPresent());
        assertEquals("Тестовое задание", tasks.get().getTitle());  // Check task name
    }
}
