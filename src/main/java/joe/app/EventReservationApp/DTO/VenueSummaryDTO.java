package joe.app.EventReservationApp.DTO;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VenueSummaryDTO {
    private UUID venueId;
    private String name;
    private String location;
    private int stars;
    private String description;
}
