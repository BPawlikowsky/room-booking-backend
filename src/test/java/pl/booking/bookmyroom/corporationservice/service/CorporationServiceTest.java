package pl.booking.bookmyroom.corporationservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationCreateException;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationLoginException;
import pl.booking.bookmyroom.corporationservice.model.Corporation;
import pl.booking.bookmyroom.corporationservice.model.requests.CreateCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.requests.LoginCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.responses.CorporationResponse;
import pl.booking.bookmyroom.corporationservice.repository.CorporationRepository;
import pl.booking.bookmyroom.security.model.UserRole;
import pl.booking.bookmyroom.userservice.exceptions.CreateUserException;
import pl.booking.bookmyroom.userservice.model.requests.CreateUserRequest;
import pl.booking.bookmyroom.userservice.model.responses.UserResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CorporationServiceTest {

    private Corporation corporation;
    @Autowired
    private CorporationRepository corporationRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final CorporationService corporationService = new CorporationService(corporationRepository, passwordEncoder);

    @BeforeEach
    public void setupDatabase() {
        corporation = new Corporation();
        corporation.setName("Test Corp.");
        corporation.setUsername("test01@test.com");
        String encodedPassword = passwordEncoder.encode("test01");
        corporation.setPassword(encodedPassword);
        corporation.setRole(UserRole.CORP);
        if(corporationRepository.findByUsername(corporation.getUsername()).isEmpty())
            corporationRepository.save(corporation);
    }

    @AfterEach
    public void cleanupDatabase() {
        corporationRepository.delete(corporation);
    }

    @Test
    void createCorporation_CREATED() throws CorporationCreateException {
        corporationRepository.delete(corporation);
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "test01@test.com",
                "test01",
                "test01"
        );
        CorporationResponse expectedResponse = new CorporationResponse(
                request.getUsername(),
                "Corporation " + request.getName() + " created"
        );
        CorporationResponse actualResponse = corporationService.createCorporation(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void createCorporation_BAD_REQUEST_No_username() {
        corporationRepository.delete(corporation);
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "",
                "test01",
                "test01"
        );
        assertThrows(CorporationCreateException.class, () -> corporationService.createCorporation(request));
    }

    @Test
    void createCorporation_BAD_REQUEST_No_password() {
        corporationRepository.delete(corporation);
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "test01@test.com",
                "",
                "test01"
        );
        assertThrows(CorporationCreateException.class, () -> corporationService.createCorporation(request));
    }

    @Test
    void createCorporation_BAD_REQUEST_Validation_no_match() throws CreateUserException {
        corporationRepository.delete(corporation);
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "test01@test.com",
                "test01",
                "test02"
        );
        assertThrows(CorporationCreateException.class, () -> corporationService.createCorporation(request));
    }

    @Test
    void loginCorporation_OK() throws CorporationLoginException {
        LoginCorporationRequest request = new LoginCorporationRequest(
                "test01@test.com",
                "test01"
        );
        CorporationResponse expectedResponse = new CorporationResponse(
                request.getUsername(),
                "Corporation " + request.getUsername() + " successfully logged in."
        );
        CorporationResponse actualResponse = corporationService.loginCorporation(request);
        assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void loginCorporation_BAD_REQUEST_No_username() {
        LoginCorporationRequest request = new LoginCorporationRequest(
                "",
                "test01"
        );
        assertThrows(CorporationLoginException.class, () -> corporationService.loginCorporation(request));
    }

    @Test
    void loginCorporation_BAD_REQUEST_No_password() {
        LoginCorporationRequest request = new LoginCorporationRequest(
                "test01@test.com",
                ""
        );
        assertThrows(CorporationLoginException.class, () -> corporationService.loginCorporation(request));
    }

    @Test
    void loginCorporation_BAD_REQUEST_Not_found() {
        LoginCorporationRequest request = new LoginCorporationRequest(
                "test02@test.com",
                "test01"
        );
        assertThrows(CorporationLoginException.class, () -> corporationService.loginCorporation(request));
    }

    @Test
    void getCorporationByUsername_found() throws UsernameNotFoundException {
        String request = "test01@test.com";
        Corporation response = corporationService.getCorporationByUsername(request);
        assertEquals(corporation.getUsername(), request);
    }

    @Test
    void getCorporationByUsername_not_found() throws UsernameNotFoundException {
        String request = "test02@test.com";
        assertThrows(UsernameNotFoundException.class, () -> corporationService.getCorporationByUsername(request));
    }
}