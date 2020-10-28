package pl.booking.bookmyroom.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.corporationservice.repository.CorporationRepository;
import pl.booking.bookmyroom.userservice.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private CorporationRepository corporationRepository;

    public MyUserDetailsService(UserRepository repository, CorporationRepository corporationRepository) {
        this.userRepository = repository;
        this.corporationRepository = corporationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userRepository.findByUsername(username).isPresent())
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
        else
            return corporationRepository.findCorporationByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Corporation " + username + " not found"));
    }


}
