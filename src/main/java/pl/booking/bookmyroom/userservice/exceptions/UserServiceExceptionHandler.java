package pl.booking.bookmyroom.userservice.exceptions;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class UserServiceExceptionHandler extends ResponseEntityExceptionHandler {
    //private final Logger logger = LoggerFactory.getLogger(UserServiceExceptionHandler.class);
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        JsonObject responseBody = new JsonObject();
        FieldError error = (FieldError) ex.getBindingResult().getAllErrors().get(0);
        responseBody.addProperty("error", "err-invalidreq");
        responseBody.addProperty(
                "message",
                "Invalid field in request: " + error.getField() + " " +
                        error.getDefaultMessage()
                );
        List<ObjectError> errors = Collections.emptyList();
        if(ex.getBindingResult().getErrorCount() > 0)
            errors = ex.getBindingResult().getAllErrors();
        else
            errors.add(new ObjectError("", ""));

        responseBody.addProperty(
                "details",
                errors.get(0).toString()
        );

        responseBody.addProperty("path", ((ServletWebRequest)request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(CreateUserException.class)
    protected ResponseEntity<Object> handleUserCreation(CreateUserException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-usrcreate");
        responseBody.addProperty("message", "Create user request: " + ex.getMessage());
        switch(ex.getMessage()) {
            case "No username given.":
                responseBody.addProperty(
                        "details",
                        "No username has been given, has to be a email account."
                ); break;
            case "No password given.":
                responseBody.addProperty(
                        "details",
                        "Password has to be at least 1 character long."
                ); break;
            case "Password doesn't match verification.":
                responseBody.addProperty(
                        "details",
                        "Password and verification have to match."
                ); break;
            default:
                responseBody.addProperty(
                        "details",
                        "Username already exists in the database."
                ); break;
        }

        responseBody.addProperty("path", ((ServletWebRequest)request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(LoginUserException.class)
    protected ResponseEntity<Object> handleUserLogin(UsernameNotFoundException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-usrlogin");
        responseBody.addProperty("message", "Login user request: " + ex.getMessage());
        responseBody.addProperty(
                        "details",
                        "Username could not be found in the database."
                );

        responseBody.addProperty("path", ((ServletWebRequest)request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }
}
