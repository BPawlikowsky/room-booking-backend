package pl.booking.bookmyroom.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.security.model.Authority;
import pl.booking.bookmyroom.security.repository.AuthRepository;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.repository.UserRepository;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;

    public MyUserDetailsService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user;
        if(userRepository.findByEmail(email).size() <= 0)
            throw new UsernameNotFoundException(email + " not found");
        else {
            user = userRepository.findByEmail(email).get(0);
            Authority authority = new Authority();
            authority.setEmail(user.getEmail());

            List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles());
            authority.setAuthority(auth.toString());
            if (authRepository.findByEmail(authority.getEmail()).size() <= 0)
                authRepository.save(authority);
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    auth);
        }
    }


}
