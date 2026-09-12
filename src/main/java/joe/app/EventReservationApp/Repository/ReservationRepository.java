package joe.app.EventReservationApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import joe.app.EventReservationApp.Model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByCustomerId(UUID customerId);

    List<Reservation> findByCustomerIdAndEventEndTimeBefore(UUID customerId, LocalDateTime endTime);

}
