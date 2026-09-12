package joe.app.EventReservationApp.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import joe.app.EventReservationApp.DTO.CreateVenueDTO;
import joe.app.EventReservationApp.DTO.EventSummaryDTO;
import joe.app.EventReservationApp.DTO.VenueSummaryDTO;
import joe.app.EventReservationApp.Service.EventService;
import joe.app.EventReservationApp.Service.VenueService;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
    @Autowired
    private VenueService venueService;
    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<VenueSummaryDTO>> getAllVenues() {
        List<VenueSummaryDTO> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{venueId}")
    public ResponseEntity<VenueSummaryDTO> getVenueById(@PathVariable UUID venueId) {
        VenueSummaryDTO venue = venueService.getVenueById(venueId);
        return ResponseEntity.ok(venue);
    }

    @GetMapping("/{venueId}/events")
    public ResponseEntity<List<EventSummaryDTO>> getEventsByVenueId(@PathVariable UUID venueId) {
        return ResponseEntity.ok(eventService.getEventsByVenuId(venueId));
    }
}
