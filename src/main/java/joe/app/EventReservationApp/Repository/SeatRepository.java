package joe.app.EventReservationApp.Repository;

import joe.app.EventReservationApp.Model.Seat;
import joe.app.EventReservationApp.Enum.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByEventId(UUID eventId);
    int countByEventIdAndStatus(UUID eventId, SeatStatus status);
}
