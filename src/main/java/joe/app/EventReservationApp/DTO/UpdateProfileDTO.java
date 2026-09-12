package joe.app.EventReservationApp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProfileDTO {
    private String username;
    private String email;
    private String name;
    private String password;
}
