package joe.app.EventReservationApp.Controller;

import java.util.List;
import java.util.UUID;

import joe.app.EventReservationApp.DTO.SeatResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import joe.app.EventReservationApp.DTO.EventDetailDTO;
import joe.app.EventReservationApp.DTO.EventSummaryDTO;
import joe.app.EventReservationApp.Service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventSummaryDTO>> getAllEvents() {
        List<EventSummaryDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailDTO> getEventById(@PathVariable UUID eventId) {
        EventDetailDTO event = eventService.getEventDetailsById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<SeatResponseDTO>> getEventSeatsById(@PathVariable UUID eventId) {
        List<SeatResponseDTO> seats = eventService.getSeatsByEventId(eventId);
        return ResponseEntity.ok(seats);
    }
    
}

