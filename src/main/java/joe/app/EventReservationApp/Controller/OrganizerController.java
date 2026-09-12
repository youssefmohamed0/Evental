package joe.app.EventReservationApp.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import joe.app.EventReservationApp.DTO.CreateEventRequestDTO;
import joe.app.EventReservationApp.DTO.EventDetailDTO;
import joe.app.EventReservationApp.DTO.EventSummaryDTO;
import joe.app.EventReservationApp.Service.EventService;

@RestController 
@RequestMapping("/api/organizer")

public class OrganizerController {

    @Autowired 
    private EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<EventSummaryDTO> createEvent(@RequestBody CreateEventRequestDTO requestDTO, Authentication authentication) {
        EventSummaryDTO createdEvent = eventService.createEvent(requestDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<EventDetailDTO> updateEvent(@PathVariable UUID eventId, @RequestBody CreateEventRequestDTO requestDTO) {
        EventDetailDTO updatedEvent = eventService.updateEventDetails(eventId, requestDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventSummaryDTO>> getEventsByOrganizer(Authentication authentication) {
        String organizerUsername = authentication.getName();
        List<EventSummaryDTO> events = eventService.getEventsByOrganizerUsername(organizerUsername);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/events/{eventId}") // redundant: could be same as public endpoint
    public ResponseEntity<EventDetailDTO> getEventDetails(@PathVariable UUID eventId) {
        EventDetailDTO event = eventService.getEventDetailsById(eventId);
        return ResponseEntity.ok(event);
    }
}
