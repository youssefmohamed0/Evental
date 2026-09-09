package joe.app.EventReservationApp.Controller;

import jakarta.transaction.Transactional;
import joe.app.EventReservationApp.DTO.AuthResponseDTO;
import joe.app.EventReservationApp.DTO.LoginRequestDTO;
import joe.app.EventReservationApp.DTO.RefreshRequestDTO;
import joe.app.EventReservationApp.DTO.SignupRequestDTO;
import joe.app.EventReservationApp.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            return ResponseEntity.ok(authenticationService.signup(signupRequestDTO));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            return ResponseEntity.ok(authenticationService.login(loginRequestDTO));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshRequestDTO request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody RefreshRequestDTO request) {
        authenticationService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Log out successful");
    }
}
