package pl.booking.bookmyroom.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.security.model.LoginStatus;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.UserLogInRequest;
import pl.booking.bookmyroom.userservice.model.UserRegistrationRequest;
import pl.booking.bookmyroom.userservice.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    LoginStatus loginStatus;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<String> registerNewUser (@RequestBody @Valid UserRegistrationRequest request){
        if(!userService.createNewUser(request))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> tryLogIn(@RequestBody@Valid UserLogInRequest request){
        if(!userService.LogIn(request))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //Todo: Delete in production code
    @GetMapping(value = "/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<User>> showAll() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
