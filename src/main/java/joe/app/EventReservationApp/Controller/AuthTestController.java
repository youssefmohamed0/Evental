package joe.app.EventReservationApp.Controller;

import joe.app.EventReservationApp.DTO.AuthTestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth-test")
public class AuthTestController {
    @GetMapping()
    public ResponseEntity<AuthTestDTO> testAuth(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN_ROLE");
        AuthTestDTO authTestDTO = new AuthTestDTO();
        authTestDTO.setUsername(username);
        authTestDTO.setRole(role);
        return ResponseEntity.ok(authTestDTO);
    }
}
