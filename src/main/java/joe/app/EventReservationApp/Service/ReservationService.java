package joe.app.EventReservationApp.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import joe.app.EventReservationApp.DTO.CreateReservationRequestDTO;
import joe.app.EventReservationApp.DTO.CreateReservationResponseDTO;
import joe.app.EventReservationApp.DTO.ReservationDetailDTO;
import joe.app.EventReservationApp.DTO.ReservationSummaryDTO;
import joe.app.EventReservationApp.DTO.SeatResponseDTO;
import joe.app.EventReservationApp.Enum.ReservationStatus;
import joe.app.EventReservationApp.Enum.SeatStatus;
import joe.app.EventReservationApp.Exception.EventNotFoundException;
import joe.app.EventReservationApp.Mapper.ReservationMapper;
import joe.app.EventReservationApp.Model.Event;
import joe.app.EventReservationApp.Model.Reservation;
import joe.app.EventReservationApp.Model.ReservationItem;
import joe.app.EventReservationApp.Model.Seat;
import joe.app.EventReservationApp.Model.User;
import joe.app.EventReservationApp.Repository.EventRepository;
import joe.app.EventReservationApp.Repository.ReservationRepository;
import joe.app.EventReservationApp.Repository.SeatRepository;
import joe.app.EventReservationApp.Repository.UserRepository;

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
                throw new IllegalArgumentException("Seat " + seat.getId() + " does not belong to event " + eventId);
            }
            if (seat.getStatus() != SeatStatus.AVAILABLE) { // availability check
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is no longer available.");
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

    public ReservationDetailDTO getReservationById(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));
        return reservationMapper.toReservationDetailDTO(reservation);
    }

    @Transactional
    public void cancelReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));
        reservation.setStatus(ReservationStatus.CANCELLED);
        // TODO: refundS?
        for (ReservationItem item : reservation.getItems()) {
            item.getSeat().setStatus(SeatStatus.AVAILABLE);
        }
        reservationRepository.save(reservation);
    }

}
