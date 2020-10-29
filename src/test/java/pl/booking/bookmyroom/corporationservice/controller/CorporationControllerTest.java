package pl.booking.bookmyroom.corporationservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationCreateException;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationLoginException;
import pl.booking.bookmyroom.corporationservice.model.requests.CreateCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.requests.LoginCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.responses.CorporationResponse;
import pl.booking.bookmyroom.corporationservice.repository.CorporationRepository;
import pl.booking.bookmyroom.corporationservice.service.CorporationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CorporationControllerTest {

    @Mock
    private CorporationRepository corporationRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Spy
    private final CorporationService corporationService =
            new CorporationService(corporationRepository, passwordEncoder);

    @InjectMocks
    private CorporationController corporationController;

    @BeforeEach
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCorporation_CREATED() throws CorporationCreateException {
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "test01@test.com",
                "test01",
                "test01"
        );

        CorporationResponse response = new CorporationResponse(
                request.getUsername(),
                "Corporation " + request.getName() + " created"
        );
        doReturn(response).when(corporationService).createCorporation(request);
        ResponseEntity<CorporationResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.CREATED);
        ResponseEntity<CorporationResponse> actualResponse = corporationController.createCorporation(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void createCorporation_BAD_REQUEST_No_name() throws CorporationCreateException {
        CreateCorporationRequest request = new CreateCorporationRequest(
                "",
                "test01@test.com",
                "test01",
                "test01"
        );
        doThrow(CorporationCreateException.class).when(corporationService).createCorporation(request);
        assertThrows(CorporationCreateException.class, () -> corporationController.createCorporation(request));
    }

    @Test
    void createCorporation_BAD_REQUEST_No_username() throws CorporationCreateException {
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "",
                "test01",
                "test01"
        );
        doThrow(CorporationCreateException.class).when(corporationService).createCorporation(request);
        assertThrows(CorporationCreateException.class, () -> corporationController.createCorporation(request));
    }

    @Test
    void createCorporation_BAD_REQUEST_No_password() throws CorporationCreateException {
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "test01@test.com",
                "",
                "test01"
        );
        doThrow(CorporationCreateException.class).when(corporationService).createCorporation(request);
        assertThrows(CorporationCreateException.class, () -> corporationController.createCorporation(request));
    }

    @Test
    void createCorporation_BAD_REQUEST_No_verification_match() throws CorporationCreateException {
        CreateCorporationRequest request = new CreateCorporationRequest(
                "Test Corp.",
                "test01@test.com",
                "test01",
                "test02"
        );
        doThrow(CorporationCreateException.class).when(corporationService).createCorporation(request);
        assertThrows(CorporationCreateException.class, () -> corporationController.createCorporation(request));
    }

    @Test
    void loginCorporation_OK() throws CorporationLoginException {
        LoginCorporationRequest request = new LoginCorporationRequest(
            "test01@test.com",
            "test01"
        );
        CorporationResponse response = new CorporationResponse(
                request.getUsername(),
                "Corporation " + request.getUsername() + " successfully logged in"
        );
        ResponseEntity<CorporationResponse> expectedResponse = new ResponseEntity<>(response, HttpStatus.OK);
        doReturn(response).when(corporationService).loginCorporation(request);
        ResponseEntity<CorporationResponse> actualResponse = corporationController.loginCorporation(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void loginCorporation_BAD_REQUEST() throws CorporationLoginException {
        LoginCorporationRequest request = new LoginCorporationRequest(
                "",
                "test01"
        );
        doThrow(CorporationLoginException.class).when(corporationService).loginCorporation(request);
        assertThrows(CorporationLoginException.class, () -> corporationController.loginCorporation(request));
    }
}