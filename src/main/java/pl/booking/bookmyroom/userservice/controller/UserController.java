package pl.booking.bookmyroom.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.requests.LoginUserRequest;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.model.responses.UserResponse;
import pl.booking.bookmyroom.userservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser (@Valid @RequestBody CreateUserRequest request)
            throws CreateUserException {
        UserResponse response = userService.createUser(request);
        if(response.getStatus().equals("User created."))
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // A login checker to verify to the front that a user exists and
    // can login with the needed credentials. This post request has to
    // have an accompanying basic authentication in header of request.
    @PostMapping(value = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody LoginUserRequest request){
        UserResponse response = userService.loginUser(request);
        if(response.getStatus().equals("User " + request.getUsername() + " successfully logged in."))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    //Todo: Delete in production code
    @GetMapping(value = "/users")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<User>> allUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
