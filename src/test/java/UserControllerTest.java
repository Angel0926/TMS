import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.TaskManagementApplication;
import org.example.controller.UserController;
import org.example.models.User;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TaskManagementApplication.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private String validToken;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        validToken = "valid-jwt-token"; // Example token, can be generated dynamically
    }

    @Test
    void testRegisterUser() throws Exception {
        User user = new User("test@example.com", "password123");
        user.setUsername("Test User");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testLoginUser() throws Exception {
        User user = new User("test@example.com", "password123");


        when(userService.authenticateUser("test@example.com", "password123")).thenReturn(validToken);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(validToken));
    }

    @Test
    void testGetCurrentUserWithValidToken() throws Exception {
        User user = new User("test@example.com", "password123");
        user.setUsername("Test User");

        when(jwtUtil.extractEmail(validToken)).thenReturn("test@example.com");

        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("Test User"));
    }

    @Test
    void testGetCurrentUserWithInvalidToken() throws Exception {
        // Simulate invalid token (e.g., expired or malformed token)
        when(jwtUtil.extractEmail("invalid-token")).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

}
