package joe.app.EventReservationApp.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import joe.app.EventReservationApp.DTO.CreateEventRequestDTO;
import joe.app.EventReservationApp.DTO.EventDetailDTO;
import joe.app.EventReservationApp.DTO.EventSummaryDTO;
import joe.app.EventReservationApp.DTO.ReservationDetailDTO;
import joe.app.EventReservationApp.DTO.ReservationSummaryDTO;
import joe.app.EventReservationApp.Service.EventService;
import joe.app.EventReservationApp.Service.ReservationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ReservationService reservationService;

// redundant: could be same as public endpoint
    @GetMapping("/events")
    public ResponseEntity<List<EventSummaryDTO>> getAllEvents() {
        List<EventSummaryDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
// redundant: could be same as public endpoint
    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventDetailDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailDTO event = eventService.getEventDetailsById(eventId);
        return ResponseEntity.ok(event);
    }

    // @PostMapping("/events")
    // public ResponseEntity<EventDetailDTO> createEvent(@RequestBody
    // CreateEventRequestDTO requestDTO, Authentication authentication) {
    // EventDetailDTO event = eventService.createEvent(requestDTO, authentication);
    // return ResponseEntity.status(HttpStatus.CREATED).body(event);
    // }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<EventDetailDTO> updateEvent(@PathVariable UUID eventId,
            @RequestBody CreateEventRequestDTO requestDTO) {
        EventDetailDTO event = eventService.updateEventDetails(eventId, requestDTO);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationSummaryDTO>> getAllReservations() {
        List<ReservationSummaryDTO> response = reservationService.getAllReservations();
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

    @GetMapping("/events/{eventId}/reservations")
    public ResponseEntity<List<ReservationSummaryDTO>> getReservationsByEventId(@PathVariable UUID eventId, Authentication authentication) {
        List<ReservationSummaryDTO> response = reservationService.getReservationsByEventId(eventId,authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/venues/{venueId}/reservations")
    public ResponseEntity<List<ReservationSummaryDTO>> getReservationsByVenueId(@PathVariable UUID venueId) {
        List<ReservationSummaryDTO> response = reservationService.getReservationsByVenueId(venueId);
        return ResponseEntity.ok(response);
    }

    // @GetMapping("/users")
    // public ResponseEntity<List<UserSummaryDTO>> getAllUsers() {
    //     List<UserSummaryDTO> response = userService.getAllUsers();
    //     return ResponseEntity.ok(response);
    // }

    // @GetMapping("/users/{userId}")
    // public ResponseEntity<UserDetailDTO> getUserById(@PathVariable UUID userId, Authentication authentication) {
    //     UserDetailDTO response = userService.getUserById(userId, authentication);
    //     return ResponseEntity.ok(response);
    // }

}
