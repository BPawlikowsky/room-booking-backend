package pl.booking.bookmyroom.corporationservice.exceptions;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CorporationServiceExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(CorporationServiceExceptionHandler.class);

    @ExceptionHandler(CorporationCreateException.class)
    protected ResponseEntity<Object> handleCorpCreation(CorporationCreateException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-corpcreate");
        responseBody.addProperty("message", "Create corporation request: " + ex.getMessage());

        switch (ex.getMessage()) {
            case "No username given.":
                responseBody.addProperty(
                        "details",
                        "No username has been given, has to be a email account."
                );
                break;
            case "No password given.":
                responseBody.addProperty(
                        "details",
                        "Password has to be at least 1 character long."
                );
                break;
            case "Verification does not match password.":
                responseBody.addProperty(
                        "details",
                        "Password and verification have to match."
                );
                break;
            default:
                responseBody.addProperty(
                        "details",
                        "Username already exists in the database."
                );
                break;
        }

        responseBody.addProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(CorporationLoginException.class)
    protected ResponseEntity<Object> handleCorpLogin(CorporationLoginException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-corplogin");
        responseBody.addProperty("message", "Login corporation request: " + ex.getMessage());
        responseBody.addProperty("details", "Could not find corporation in the database.");
        responseBody.addProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }
}
