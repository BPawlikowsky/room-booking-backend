package pl.booking.bookmyroom.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.security.model.LoginStatus;
import pl.booking.bookmyroom.security.repository.AuthRepository;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.model.UserLogInRequest;
import pl.booking.bookmyroom.userservice.model.UserRegistrationRequest;
import pl.booking.bookmyroom.userservice.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    LoginStatus loginStatus;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private AuthRepository authRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean createNewUser(UserRegistrationRequest request) {
        if(!userRepository.findByEmail(request.getEmail()).isEmpty()) {
            return false;
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword());

        User user = new User();
        user.setActive(true);
        user.setRoles("USER");
        user.setEmail(request.getEmail());
        if(request.getPassword().equals(request.getPasswordVerify())){
            String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
        } else {
            return false;
        }

        token.setDetails(user);
        userRepository.save(user);
        try {
            Authentication auth = authManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (BadCredentialsException e) {
            System.out.println("Bad Credentials Exception: " + e.getMessage());
        }
        
        return true;
    }

    public boolean LogIn(UserLogInRequest request) {
        if(userRepository.findByEmail(request.getEmail())
                .stream()
                .allMatch(u -> bCryptPasswordEncoder.matches(request.getPassword(), u.getPassword())))
        {
            UsernamePasswordAuthenticationToken authReq =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication auth;
            try{
                auth = authManager.authenticate(authReq);
            }
            catch (AuthenticationException e) {
                logger.info("Authentication error: " + e.getMessage() + " " + e.getCause().getMessage());
                return false;
            }

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

            loginStatus.setLoggedIn(true);
            loginStatus.setUsername(request.getEmail());

            userRepository.findByEmail(request.getEmail()).forEach(u -> loginStatus.setUserId(u.getId()));
            userRepository.findByEmail(request.getEmail()).forEach(u -> loginStatus.setUserType(u.getRoles()));
            return true;
        } else return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
