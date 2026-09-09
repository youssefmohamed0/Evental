package joe.app.EventReservationApp.DTO;

import joe.app.EventReservationApp.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthTestDTO {
    String username;
    String role;
}
