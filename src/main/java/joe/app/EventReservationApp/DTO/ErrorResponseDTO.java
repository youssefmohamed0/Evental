package joe.app.EventReservationApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String msg;
    private int statusCode;
    private String requestURI;
    private LocalDateTime timestamp;
}
