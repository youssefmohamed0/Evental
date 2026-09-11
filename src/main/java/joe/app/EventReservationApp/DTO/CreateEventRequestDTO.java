package joe.app.EventReservationApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequestDTO {
    private UUID venueId;
    private String name;
    private String theme;
    private String description;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private BigDecimal ticketPrice;
    private long seatCapacity;
}
