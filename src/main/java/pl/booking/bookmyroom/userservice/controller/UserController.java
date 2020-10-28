package pl.booking.bookmyroom.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.exceptions.LoginUserException;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.requests.LoginUserRequest;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.model.responses.UserResponse;
import pl.booking.bookmyroom.userservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser (@Valid @RequestBody CreateUserRequest request)
            throws CreateUserException {
        UserResponse response = userService.createUser(request);
        if(response.getStatus().equals("User created."))
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Should be accompanied by basic authentication
    @PostMapping(value = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginUserRequest request) throws LoginUserException {
        UserResponse response = userService.loginUser(request);
        if(response.getStatus().equals("User " + request.getUsername() + " successfully logged in."))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //Todo: Delete in production code
    @GetMapping(value = "/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<User>> allUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
