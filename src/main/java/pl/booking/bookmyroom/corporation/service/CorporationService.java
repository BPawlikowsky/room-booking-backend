package pl.booking.bookmyroom.corporation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.booking.bookmyroom.corporation.model.Corporation;
import pl.booking.bookmyroom.corporation.model.CreateCorporationRequest;
import pl.booking.bookmyroom.corporation.model.CorporationRepository;
import pl.booking.bookmyroom.corporation.model.LoginCorporationRequest;
import pl.booking.bookmyroom.security.model.LoginStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class CorporationService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CorporationRepository corporationRepository;

    @Autowired
    public CorporationService(CorporationRepository corporationRepository) {
        this.corporationRepository = corporationRepository;
    }

    public boolean addCorporation(CreateCorporationRequest request) {
        if(!getCorporationByEmail(request.getEmail()).isEmpty()) {
            return false;
        } else {
            Corporation corporation = new Corporation();
            corporation.setName(request.getName());
            if(request.getPassword().equals(request.getRepeatedPassword())) {
                String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
                corporation.setPassword(encodedPassword);
            } else {
                return false;
            }
            corporation.setEmail(request.getEmail());
            corporation.setRoles("CORPO");
            corporationRepository.save(corporation);
            return true;
        }
    }

    public boolean loginCorporation(HttpServletRequest sReq, LoginCorporationRequest request) {
        if(getCorporationByEmail(request.getEmail()).stream()
                .anyMatch(c -> bCryptPasswordEncoder.matches(request.getPassword(), c.getPassword()))) {
            return true;
        } else return false;
    }

    public List<Corporation> getAllCorporations(){
        return corporationRepository.findAll();
    }

    public List<Corporation> getCorporationByEmail(String email){
        return corporationRepository.findCorporationByEmail(email);
    }
}
