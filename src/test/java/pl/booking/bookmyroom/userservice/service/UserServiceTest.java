package pl.booking.bookmyroom.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.security.model.UserRole;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.model.requests.LoginUserRequest;
import pl.booking.bookmyroom.userservice.model.responses.UserResponse;
import pl.booking.bookmyroom.userservice.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Bean
    public UserRepository getUserRepository() {
        return mock(UserRepository.class);
    }

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUser_CREATED() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "test01",
                "test01"
        );
        UserResponse expectedResponse = new UserResponse(
                request.getUsername(),
                "User " + request.getUsername() + " created."
        );
        UserResponse actualResponse = userService.createUser(request);
        assertEquals(actualResponse.getUsername(), expectedResponse.getUsername());
        assertEquals(actualResponse.getStatus(), expectedResponse.getStatus());
    }

    @Test
    void createUser_BAD_REQUEST_No_username() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "",
                "test01",
                "test01"
        );
        assertThrows(CreateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_BAD_REQUEST_No_password() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "",
                "test01"
        );
        assertThrows(CreateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_BAD_REQUEST_Validation_no_match() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "test01",
                "test02"
        );
        assertThrows(CreateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void loginUser_OK() {
        LoginUserRequest request = new LoginUserRequest(
                "test01@test.com",
                "test01"
        );
        User user = new User();
        user.setUsername(request.getUsername());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(UserRole.USER);

        UserResponse expectedResponse = new UserResponse(
                request.getUsername(),
                "User " + request.getUsername() + " logged in."
        );

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        UserResponse actualResponse = userService.loginUser(request);

        assertEquals(actualResponse.getUsername(), expectedResponse.getUsername());
        assertEquals(actualResponse.getStatus(), expectedResponse.getStatus());
    }
}