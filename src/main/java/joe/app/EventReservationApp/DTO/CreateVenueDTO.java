package joe.app.EventReservationApp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateVenueDTO {
    private String name;
    private String location;
    private int stars;
    private String description;
}
