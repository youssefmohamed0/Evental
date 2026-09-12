package joe.app.EventReservationApp.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ReservationDetailDTO {
    private UUID reservationId;
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
    private String venueName;
    private List<String> seatsNumbers;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
}
