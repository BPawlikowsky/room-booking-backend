package pl.booking.bookmyroom.corporationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationCreateException;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationLoginException;
import pl.booking.bookmyroom.corporationservice.model.Corporation;
import pl.booking.bookmyroom.corporationservice.model.requests.CreateCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.requests.LoginCorporationRequest;
import pl.booking.bookmyroom.corporationservice.model.responses.CorporationResponse;
import pl.booking.bookmyroom.corporationservice.service.CorporationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/corp")
@CrossOrigin
public class CorporationController {

    private final CorporationService corporationService;

    @Autowired
    public CorporationController(CorporationService corporationService) {
        this.corporationService = corporationService;
    }

    @PostMapping(value = "/")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<CorporationResponse> createCorporation(@RequestBody @Valid CreateCorporationRequest request)
            throws CorporationCreateException {
        CorporationResponse response = corporationService.createCorporation(request);
        if(!response.getStatus().matches(".* created"))
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<CorporationResponse> loginCorporation(@RequestBody @Valid LoginCorporationRequest request)
            throws CorporationLoginException {
        CorporationResponse response = corporationService.loginCorporation(request);
        if(response.getStatus().equals("Corporation " + request.getUsername() + " successfully logged in"))
            return new ResponseEntity<>(response, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //todo delete endpoint
    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Corporation> getAllCorporations(){
        return corporationService.getAllCorporations();
    }
}