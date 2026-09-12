package joe.app.EventReservationApp.DTO;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReservationResponseDTO {
    private UUID reservationId;
    private String status;
    private BigDecimal totalPrice;
}
