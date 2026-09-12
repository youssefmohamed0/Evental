package joe.app.EventReservationApp.DTO;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationSummaryDTO {
    private UUID reservationId;
    private UUID eventId;
    private String eventName;
    private String status;
    private double totalPrice;
}
