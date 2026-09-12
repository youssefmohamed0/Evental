package joe.app.EventReservationApp.DTO;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationRequestDTO {
    private UUID eventId;
    private List<UUID> seatIds;

}
