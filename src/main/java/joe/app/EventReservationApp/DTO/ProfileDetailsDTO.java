package joe.app.EventReservationApp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProfileDetailsDTO {
    private UUID id;
    private String username;
    private String email;
    private String name;
    private LocalDateTime creationDate;
    private String role;
}
