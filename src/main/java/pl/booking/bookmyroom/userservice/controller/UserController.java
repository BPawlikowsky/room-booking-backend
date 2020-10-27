package pl.booking.bookmyroom.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.requests.UserLoginRequest;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/api/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<String> createUser (@RequestBody @Valid CreateUserRequest request){
        if(!userService.createNewUser(request))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> loginUser(@RequestBody@Valid UserLoginRequest request){
        if(!userService.LogIn(request))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //Todo: Delete in production code
    @GetMapping(value = "/api/users")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<User>> allUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
