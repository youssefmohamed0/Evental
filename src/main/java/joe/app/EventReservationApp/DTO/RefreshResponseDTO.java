package joe.app.EventReservationApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponseDTO {
    private String accessToken;
    private String refreshToken;
}
