package pl.booking.bookmyroom.userservice.service;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.security.model.UserRole;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.exceptions.LoginUserException;
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

    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @BeforeEach
    public void setupDatabase() {
        user = new User();
        user.setUsername("test01@test.com");
        String encodedPassword = passwordEncoder.encode("test01");
        user.setPassword(encodedPassword);
        user.setRole(UserRole.USER);
        if(userRepository.findByUsername(user.getUsername()).isEmpty())
            userRepository.save(user);
    }

    @AfterEach
    public void cleanupDatabase() {
        userRepository.delete(user);
    }

    @Test
    void createUser_CREATED() throws CreateUserException {
        userRepository.delete(user);
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
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void createUser_BAD_REQUEST_No_username() throws CreateUserException {
        userRepository.delete(user);
        CreateUserRequest request = new CreateUserRequest(
                "",
                "test01",
                "test01"
        );
        assertThrows(CreateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_BAD_REQUEST_No_password() throws CreateUserException {
        userRepository.delete(user);
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "",
                "test01"
        );
        assertThrows(CreateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_BAD_REQUEST_Validation_no_match() throws CreateUserException {
        userRepository.delete(user);
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "test01",
                "test02"
        );
        assertThrows(CreateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void loginUser_OK() throws LoginUserException {
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
                "User " + request.getUsername() + " successfully logged in."
        );

        UserResponse actualResponse = userService.loginUser(request);

        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void loginUser_BAD_REQUEST_No_name() {
        LoginUserRequest request = new LoginUserRequest(
                "",
                "test01"
        );
        assertThrows(LoginUserException.class, () -> userService.loginUser(request));
    }

    @Test
    void loginUser_BAD_REQUEST_No_password() {
        LoginUserRequest request = new LoginUserRequest(
                "test01@test.com",
                ""
        );
        assertThrows(LoginUserException.class, () -> userService.loginUser(request));
    }
}