package pl.booking.bookmyroom.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.security.model.UserRole;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.requests.LoginUserRequest;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.model.responses.UserResponse;
import pl.booking.bookmyroom.userservice.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

     public UserService (UserRepository repository,
                         BCryptPasswordEncoder passwordEncoder
     ) {
         this.userRepository = repository;
         this.bCryptPasswordEncoder = passwordEncoder;
     }

    public UserResponse createUser(CreateUserRequest request) throws CreateUserException {
        String status;
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            status = "User with username " + request.getUsername() + " already exists.";
            throw new CreateUserException(status);
        } else if(request.getUsername().equals("")) {
            status = "No username given.";
            throw new CreateUserException(status);
        } else if(request.getPassword().equals("")) {
            status = "No password given.";
            throw new CreateUserException(status);
        }
        else {
            User user = new User();
            user.setUsername(request.getUsername());
            if (request.getPassword().equals(request.getPasswordVerification())) {
                String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
                user.setPassword(encodedPassword);
                user.setRole(UserRole.USER);
                userRepository.save(user);
                status = "User " + user.getUsername() + " created.";
            } else {
                status = "Password doesn't match verification.";
                throw new CreateUserException(status);
            }
        }

        return new UserResponse(
                request.getUsername(),
                status
        );
    }

    public UserResponse loginUser(LoginUserRequest request) {
        String status;
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user " + request.getUsername()));
        if(userRepository.findByUsername(request.getUsername())
                .stream()
                .allMatch(u -> bCryptPasswordEncoder
                        .matches(request.getPassword(), u.getPassword()))
        ) {
            status = "User " + user.getUsername() + " successfully logged in.";
        } else {
            status = "Could not find user " + request.getUsername();
            throw new UsernameNotFoundException(status);
        }

        return new UserResponse(request.getUsername(), status);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
