package joe.app.EventReservationApp.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class UserSummaryDTO {
    private UUID userId;
    private String username;
    private String email;
    private String role;
}
