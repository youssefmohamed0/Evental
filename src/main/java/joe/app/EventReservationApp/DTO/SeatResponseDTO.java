package joe.app.EventReservationApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponseDTO {
    private UUID id;
    private String seatNumber;
    private String status;
}
