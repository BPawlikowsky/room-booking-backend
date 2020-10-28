package pl.booking.bookmyroom.corporationservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.booking.bookmyroom.corporationservice.model.Corporation;
import pl.booking.bookmyroom.corporationservice.repository.CorporationRepository;
import pl.booking.bookmyroom.corporationservice.service.CorporationService;
import pl.booking.bookmyroom.security.model.UserRole;
import pl.booking.bookmyroom.userservice.model.User;
import pl.booking.bookmyroom.userservice.repository.UserRepository;
import pl.booking.bookmyroom.userservice.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CorporationControllerTest {



    @Test
    void addCorporation() {
    }

    @Test
    void loginCorporation() {
    }

    @Test
    void getAllCorporations() {
    }
}