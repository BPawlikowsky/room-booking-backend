package pl.booking.bookmyroom.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.security.model.UserRole;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.requests.UserLoginRequest;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

     public UserService (UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
         this.userRepository = repository;
         this.bCryptPasswordEncoder = passwordEncoder;
     }

    public boolean createNewUser(CreateUserRequest request) {
        if(userRepository.findByUsername(request.getEmail()).isPresent()) {
            return false;
        }

        User user = new User();
        user.setRole(UserRole.USER);
        user.setUsername(request.getEmail());
        if(request.getPassword().equals(request.getPasswordVerify())){
            String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        } else {
            return false;
        }

        userRepository.save(user);
        
        return true;
    }

    public boolean LogIn(UserLoginRequest request) {
        if(userRepository.findByUsername(request.getEmail())
                .stream()
                .allMatch(u -> bCryptPasswordEncoder.matches(request.getPassword(), u.getPassword())))
        {
            User activeUser = userRepository.findByUsername(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException(request.getEmail() + " not found"));
            return true;
        } else return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
