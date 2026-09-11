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
import joe.app.EventReservationApp.Service.EventService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private EventService eventService;
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
}
