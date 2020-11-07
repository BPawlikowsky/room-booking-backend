package pl.booking.bookmyroom.hotelservice.exceptions;

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
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationCreateException;
import pl.booking.bookmyroom.corporationservice.exceptions.CorporationLoginException;

@ControllerAdvice
public class HotelServiceExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(HotelServiceExceptionHandler.class);

    @ExceptionHandler(CreateHotelException.class)
    protected ResponseEntity<Object> handleHotelCreation(CreateHotelException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-hotelcreate");
        responseBody.addProperty("message", "Create hotel request: " + ex.getMessage());

        switch (ex.getMessage()) {
            case "No hotel or city name":
                responseBody.addProperty(
                        "details",
                        "No hotel or city name has been given."
                );
                break;
            case "No Street name or no street number":
                responseBody.addProperty(
                        "details",
                        "No Street name or no street number has been given."
                );
                break;
            default:
                responseBody.addProperty(
                        "details",
                        "Hotel by the name in city already exists in the database."
                );
                break;
        }

        responseBody.addProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DeleteHotelException.class)
    protected ResponseEntity<Object> handleHotelDeletion(DeleteHotelException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-hoteldelete");
        responseBody.addProperty("message", "Delete hotel request: " + ex.getMessage());
        responseBody.addProperty(
                "details",
                "Hotel requested for deletion was not found."
        );
        responseBody.addProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(FindHotelException.class)
    protected ResponseEntity<Object> handleHotelFind(FindHotelException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-hotelfind");
        responseBody.addProperty("message", "Delete hotel request: " + ex.getMessage());
        responseBody.addProperty(
                "details",
                "Queried hotel was not found in the database."
        );
        responseBody.addProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EditHotelException.class)
    protected ResponseEntity<Object> handleHotelEdit(EditHotelException ex, WebRequest request) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty("error", "err-hoteledit");
        responseBody.addProperty("message", "Edit hotel request: " + ex.getMessage());
        responseBody.addProperty(
                "details",
                "Edited hotel was not found in the database."
        );
        responseBody.addProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String bodyOfResponse = responseBody.toString();
        return handleExceptionInternal(ex, bodyOfResponse, header, HttpStatus.BAD_REQUEST, request);
    }
}
