package pl.booking.bookmyroom.corporationservice.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationCreateException;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationLoginException;
import pl.booking.bookmyroom.corporationservice.model.Corporation;
import pl.booking.bookmyroom.corporationservice.model.requests.CreateCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.responses.CorporationResponse;
import pl.booking.bookmyroom.corporationservice.repository.CorporationRepository;
import pl.booking.bookmyroom.corporationservice.model.requests.LoginCorporationRequest;
import pl.booking.bookmyroom.security.model.UserRole;

import java.util.List;

@Service
public class CorporationService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CorporationRepository corporationRepository;

    public CorporationService(CorporationRepository corporationRepository,
                              BCryptPasswordEncoder passwordEncoder) {
        this.corporationRepository = corporationRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    public CorporationResponse createCorporation(CreateCorporationRequest request) throws CorporationCreateException {
        String status;
        if(corporationRepository.findCorporationByUsername(request.getUsername()).isPresent()) {
            status = "Corporation " + request.getUsername() + " already exists.";
            throw new CorporationCreateException(status);
        }
        else if(request.getName().equals("")) {
            status = "No name given.";
            throw new CorporationCreateException(status);
        }
        else if(request.getUsername().equals("")) {
            status = "No username given.";
            throw new CorporationCreateException(status);
        }
        else if(request.getPassword().equals("")) {
            status = "No password given.";
            throw new CorporationCreateException(status);
        }
        else {
            Corporation corporation = new Corporation();
            corporation.setName(request.getName());
            if(request.getPassword().equals(request.getPasswordVerification())) {
                String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
                corporation.setPassword(encodedPassword);
            } else {
                status = "Verification does not match password.";
                throw new CorporationCreateException(status);
            }
            corporation.setUsername(request.getUsername());
            corporation.setRole(UserRole.CORP);
            corporationRepository.save(corporation);
            status = "Corporation " + request.getName() + " created";
            return new CorporationResponse(
                    corporation.getUsername(),
                    status
            );
        }
    }

    public CorporationResponse loginCorporation(LoginCorporationRequest request)
            throws CorporationLoginException {
        String status;
        Corporation corporation = corporationRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new CorporationLoginException(
                                "Could not find corporation " +
                                        request.getUsername()));
        if(corporationRepository.findByUsername(request.getUsername())
                .stream()
                .allMatch(u -> bCryptPasswordEncoder
                        .matches(request.getPassword(), u.getPassword()))
        ) {
            status = "Corporation " + corporation.getUsername() + " successfully logged in";
        } else {
            status = "Could not find corporation " + request.getUsername();
            throw new CorporationLoginException(status);
        }

        return new CorporationResponse(corporation.getUsername(), status);
    }

    public List<Corporation> getAllCorporations(){
        return corporationRepository.findAll();
    }

    public Corporation getCorporationByUsername(String username) throws UsernameNotFoundException {
        return corporationRepository
                .findCorporationByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Corporation not found.")
                );
    }
}
