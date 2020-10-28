package pl.booking.bookmyroom.corporationservice.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CorporationResponse {
    private final String username;
    private final String status;
}
