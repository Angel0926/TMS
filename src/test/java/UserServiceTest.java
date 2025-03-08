import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.TaskManagementApplication;
import org.example.models.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest(classes = TaskManagementApplication.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("test@example.com", "password123");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.registerUser(testUser);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testAuthenticateUser_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testUser.getPassword(), testUser.getPassword())).thenReturn(true);

        when(jwtUtil.generateToken(testUser.getEmail())).thenReturn("sampleToken");

        String token = userService.authenticateUser(testUser.getEmail(), testUser.getPassword());

        assertNotNull(token);
        assertEquals("sampleToken", token);
    }

    @Test
    void testAuthenticateUser_Fail() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.authenticateUser(testUser.getEmail(), testUser.getPassword()));
    }
}
