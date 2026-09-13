package joe.app.EventReservationApp.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import joe.app.EventReservationApp.DTO.*;
import joe.app.EventReservationApp.Enum.*;
import joe.app.EventReservationApp.Exception.*;
import joe.app.EventReservationApp.Mapper.ReservationMapper;
import joe.app.EventReservationApp.Model.*;
import joe.app.EventReservationApp.Repository.*;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Transactional
    public CreateReservationResponseDTO createReservation(CreateReservationRequestDTO request,
            Authentication authentication) {
        UUID eventId = request.getEventId();
        List<UUID> seatIds = request.getSeatIds();

        Event event = eventRepository.getReferenceById(eventId);

        User customer = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        List<Seat> loadedSeats = seatRepository.findAllById(seatIds);
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Seat seat : loadedSeats) {
            if (!seat.getEvent().getId().equals(eventId)) {
                throw new InvalidReservationArgumentsException("Seat " + seat.getId() + " does not belong to event " + eventId);
            }
            if (seat.getStatus() != SeatStatus.AVAILABLE) { // availability check
                throw new ReservationConflictException("Seat " + seat.getSeatNumber() + " is no longer available.");
            }

            // Changing status automatically flags it to be saved because of @Transactional
            seat.setStatus(SeatStatus.RESERVED);
            totalPrice = totalPrice.add(seat.getEvent().getTicketPrice());
        }

        Reservation newReservation = new Reservation();
        newReservation.setEvent(event);
        newReservation.setCustomer(customer);
        newReservation.setStatus(ReservationStatus.PENDING);
        newReservation.setTotalPrice(totalPrice);
        newReservation.setItems(new ArrayList<>()); // prevent NullPointerException

        for (Seat seat : loadedSeats) {
            ReservationItem reservationItem = new ReservationItem();
            reservationItem.setSeat(seat);
            reservationItem.setPrice(seat.getEvent().getTicketPrice());

            reservationItem.setReservation(newReservation);
            newReservation.getItems().add(reservationItem);
        }

        // CascadeType.ALL on Reservation.items means saving the reservation saves the
        // items too.
        Reservation savedReservation = reservationRepository.save(newReservation);

        return reservationMapper.toCreateReservationResponseDTO(savedReservation);
    }

    public List<ReservationSummaryDTO> getReservationByUsername(String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
        List<Reservation> reservations = reservationRepository.findByCustomerId(customer.getId());
        return reservationMapper.toReservationSummaryDTOList(reservations);
    }

    public List<ReservationSummaryDTO> getReservationHistory(String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
        List<Reservation> reservations = reservationRepository.findByCustomerIdAndEventEndTimeBefore(customer.getId(),
                LocalDateTime.now());
        return reservationMapper.toReservationSummaryDTOList(reservations);
    }

    public ReservationDetailDTO getReservationById(UUID reservationId, Authentication authentication) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));

        authorizeReservationAccess(reservation, authentication);

        return reservationMapper.toReservationDetailDTO(reservation);
    }

    public List<ReservationSummaryDTO> getReservationsByEventId(UUID eventId, Authentication authentication) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        authorizeEventAccess(event, authentication);
        List<Reservation> reservations = reservationRepository.findByEventId(eventId);
        return reservationMapper.toReservationSummaryDTOList(reservations);
    }

    public List<ReservationSummaryDTO> getReservationsByVenueId(UUID venueId) { // for admin only
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new VenueNotFoundException("Venue not found with ID: " + venueId));

        List<Reservation> reservations = reservationRepository.findByEventVenueId(venue.getId());
        return reservationMapper.toReservationSummaryDTOList(reservations);
    }

    public List<ReservationSummaryDTO> getAllReservations() { // for admin only
        List<Reservation> reservations = reservationRepository.findAll();
        return reservationMapper.toReservationSummaryDTOList(reservations);
    }

    @Transactional
    public void cancelReservation(UUID reservationId, Authentication authentication) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));

        authorizeReservationAccess(reservation, authentication);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationConflictException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        // TODO: refundS? only if booked though
        for (ReservationItem item : reservation.getItems()) {
            item.getSeat().setStatus(SeatStatus.AVAILABLE);
        }
        reservationRepository.save(reservation);
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // Runs every 1 minute
    public void expireUnpaidReservations() {
        // Calculate the cutoff time (15 minutes ago)
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(15);

        // Fetch all PENDING reservations older than 15 minutes
        List<Reservation> expiredReservations = reservationRepository
                .findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoffTime);

        if (!expiredReservations.isEmpty()) {
            for (Reservation reservation : expiredReservations) {
                // Update reservation status
                reservation.setStatus(ReservationStatus.EXPIRED); 

                // Free up the associated seats
                for (ReservationItem item : reservation.getItems()) {
                    item.getSeat().setStatus(SeatStatus.AVAILABLE);
                }
            }
            
            reservationRepository.saveAll(expiredReservations);
            
            System.out.println("Cleaned up " + expiredReservations.size() + " expired reservations.");
        }
    }

    private void authorizeReservationAccess(Reservation reservation, Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalArgumentException("You are not logged in"));
        if (role.equals("ORGANIZER") && !reservation.getEvent().getOrganizer().getUsername().equals(username)) {
            throw new UnauthorizedReservationAccessException("You are not the organizer of this reservation");
        }
        if (role.equals("CUSTOMER") && !reservation.getCustomer().getUsername().equals(username)) {
            throw new UnauthorizedReservationAccessException("You are not the customer of this reservation");
        }
    }

    private void authorizeEventAccess(Event event, Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new UnauthorizedReservationAccessException("You are not logged in"));
        if (role.equals("ORGANIZER") && !event.getOrganizer().getUsername().equals(username)) {
            throw new UnauthorizedReservationAccessException("You are not the organizer of this event");
        }
    }

}
