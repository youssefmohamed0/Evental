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
public class EventSummaryDTO {
    private UUID id;
    private String name;
    private String theme;
    private UUID venueId;
    private String venueName;
    private int venueStars;
    private int seatCapacity;
    private BigDecimal ticketPrice;
    private LocalDateTime startTimestamp;
}
