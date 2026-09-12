package joe.app.EventReservationApp.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import joe.app.EventReservationApp.DTO.CreateReservationRequestDTO;
import joe.app.EventReservationApp.DTO.CreateReservationResponseDTO;
import joe.app.EventReservationApp.DTO.ReservationDetailDTO;
import joe.app.EventReservationApp.DTO.ReservationSummaryDTO;
import joe.app.EventReservationApp.Service.ReservationService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private ReservationService reservationService;

    // reservations

    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequestDTO request,
            Authentication authentication) {
        CreateReservationResponseDTO response = reservationService.createReservation(request, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationSummaryDTO>> getReservations(Authentication authentication) {
        List<ReservationSummaryDTO> response = reservationService.getReservationByUsername(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations/history")
    public ResponseEntity<List<ReservationSummaryDTO>> getReservationsHistory(Authentication authentication) {
        List<ReservationSummaryDTO> response = reservationService.getReservationHistory(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationDetailDTO> getReservationById(@PathVariable UUID reservationId,
            Authentication authentication) {
        ReservationDetailDTO response = reservationService.getReservationById(reservationId, authentication);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID reservationId, Authentication authentication) {
        reservationService.cancelReservation(reservationId, authentication);
        return ResponseEntity.ok(null);
    }

}
