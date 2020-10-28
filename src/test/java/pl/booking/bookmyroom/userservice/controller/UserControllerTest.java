package pl.booking.bookmyroom.userservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.model.responses.UserResponse;
import pl.booking.bookmyroom.userservice.repository.UserRepository;
import pl.booking.bookmyroom.userservice.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Spy
    private final UserService userService =
            new UserService(userRepository, passwordEncoder);

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUser_OK() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "test01",
                "test01"
        );

        UserResponse response = new UserResponse(request.getUsername(), "User created.");
        doReturn(response).when(userService).createUser(request);
        ResponseEntity<UserResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.CREATED);
        ResponseEntity<UserResponse> actualResponse = userController.createUser(request);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    void createUser_BAD_REQUEST_No_Name() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "",
                "test01",
                "test01"
        );
        doThrow(CreateUserException.class).when(userService).createUser(request);
        assertThrows(CreateUserException.class,() -> userController.createUser(request));
    }

    @Test
    void createUser_BAD_REQUEST_No_password() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "",
                "test01"
        );
        doThrow(CreateUserException.class).when(userService).createUser(request);
        assertThrows(CreateUserException.class,() -> userController.createUser(request));
    }

    @Test
    void createUser_BAD_REQUEST_Verification_no_match() throws CreateUserException {
        CreateUserRequest request = new CreateUserRequest(
                "test01@test.com",
                "test01",
                "test02"
        );
        doThrow(CreateUserException.class).when(userService).createUser(request);
        assertThrows(CreateUserException.class,() -> userController.createUser(request));
    }

    @Test
    void loginUser_OK() {
    }
}