package joe.app.EventReservationApp.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import joe.app.EventReservationApp.DTO.CreateEventRequestDTO;
import joe.app.EventReservationApp.DTO.EventDetailDTO;
import joe.app.EventReservationApp.DTO.EventSummaryDTO;
import joe.app.EventReservationApp.Enum.Role;
import joe.app.EventReservationApp.Enum.SeatStatus;
import joe.app.EventReservationApp.Exception.EventNotFoundException;
import joe.app.EventReservationApp.Mapper.EventMapper;
import joe.app.EventReservationApp.Model.Event;
import joe.app.EventReservationApp.Model.Seat;
import joe.app.EventReservationApp.Model.User;
import joe.app.EventReservationApp.Model.Venue;
import joe.app.EventReservationApp.Repository.EventRepository;
import joe.app.EventReservationApp.Repository.SeatRepository;
import joe.app.EventReservationApp.Repository.UserRepository;
import joe.app.EventReservationApp.Repository.VenueRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private UserRepository userRepository;

    public List<EventSummaryDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> eventMapper.toEventSummaryDTO(event, event.getSeats().size()))
                .collect(Collectors.toList());
    }

    public EventDetailDTO getEventDetailsById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        long availableSeats = event.getSeats().stream()
                .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                .count();
        return eventMapper.toEventDetailDTO(event, availableSeats);
    }

    // ORGANIZER
    public List<EventSummaryDTO> getEventsByOrganizerUsername(String organizerUsername) {
        return eventRepository.findByOrganizerUsername(organizerUsername).stream()
                .map(event -> eventMapper.toEventSummaryDTO(event, event.getSeats().size()))
                .collect(Collectors.toList());
    }

    public EventDetailDTO updateEventDetails(UUID eventId, CreateEventRequestDTO requestDTO) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        // if (requestDTO.getSeatCapacity() < event.getSeats().stream()
        //         .filter(seat -> seat.getStatus() == SeatStatus.RESERVED || seat.getStatus() == SeatStatus.BOOKED)
        //         .count()) {
        //     throw new RuntimeException("Event seat capacity cannot be less than reserved or booked seats");
        // }
        checkEventValidity(requestDTO);
        event.setName(requestDTO.getName());
        event.setStartTime(requestDTO.getStartTimestamp());
        event.setEndTime(requestDTO.getEndTimestamp());
        event.setTicketPrice(requestDTO.getTicketPrice()); // cant update seat capacity because you will have to remove or add specifc seat which is a pain in the butt
        // event.setSeatCapacity(requestDTO.getSeatCapacity());
        event.setDescription(requestDTO.getDescription());
        event.setTheme(requestDTO.getTheme());
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toEventDetailDTO(savedEvent, savedEvent.getSeats().size());
    }

    public EventDetailDTO createEvent(CreateEventRequestDTO requestDTO, Authentication authentication) {
        String organizerUsername = authentication.getName();
        checkEventValidity(requestDTO);
        Venue venue = venueRepository.findById(requestDTO.getVenueId())
                .orElseThrow(() -> new RuntimeException("Venue not found with id: " + requestDTO.getVenueId()));
        Event event = eventMapper.toEvent(requestDTO);
        event.setVenue(venue);
        User organizer = userRepository.findByUsername(organizerUsername)
                .orElseThrow(() -> new RuntimeException("Organizer not found with username: " + organizerUsername));

        if (organizer.getRole() != Role.ORGANIZER) { // should never happen anyways
            throw new RuntimeException("User " + organizerUsername + " is not an organizer");
        }
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);

        // Create seats for the event
        createSeatsForEvent(savedEvent);

        return eventMapper.toEventDetailDTO(savedEvent, savedEvent.getSeatCapacity());
    }

    public void deleteEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        if (event.getSeats().stream()
                .filter(seat -> seat.getStatus() == SeatStatus.RESERVED || seat.getStatus() == SeatStatus.BOOKED)
                .count() > 0) {
            throw new RuntimeException("Event cannot be deleted because it has reserved or booked seats");
        }
        eventRepository.delete(event);
    }

    private void createSeatsForEvent(Event event) { // TODO this is very costly
        for (int i = 1; i <= event.getSeatCapacity(); i++) {
            Seat seat = new Seat();
            seat.setEvent(event);
            seat.setSeatNumber(String.valueOf(i));
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setVersion(0);
            seatRepository.save(seat);
        }
    }

    private void checkEventValidity(CreateEventRequestDTO requestDTO) {
        if (requestDTO.getStartTimestamp().isAfter(requestDTO.getEndTimestamp())) {
            throw new RuntimeException("Event start time cannot be after event end time");
        }
        if (requestDTO.getEndTimestamp().isBefore(requestDTO.getStartTimestamp())) {
            throw new RuntimeException("Event end time cannot be before event start time");
        }
        // if(requestDTO.getStartTimestamp().isBefore(requestDTO.getEndTimestamp().minusHours(1)))
        // {
        // throw new RuntimeException("Event duration must be at least 1 hour");
        // }
        if (requestDTO.getStartTimestamp().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event start time cannot be in the past");
        }
        if (requestDTO.getEndTimestamp().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event end time cannot be in the past");
        }
    }
}
