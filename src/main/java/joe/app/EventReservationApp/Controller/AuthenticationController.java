package joe.app.EventReservationApp.Controller;

import joe.app.EventReservationApp.DTO.AuthResponseDTO;
import joe.app.EventReservationApp.DTO.LoginRequestDTO;
import joe.app.EventReservationApp.DTO.RefreshRequestDTO;
import joe.app.EventReservationApp.DTO.SignupRequestDTO;
import joe.app.EventReservationApp.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(authenticationService.signup(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshRequestDTO request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        authenticationService.logout(username);
        return ResponseEntity.ok("Log out successful");
    }
}

