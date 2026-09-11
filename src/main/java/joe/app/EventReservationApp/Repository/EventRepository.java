package joe.app.EventReservationApp.Repository;

import joe.app.EventReservationApp.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findByVenueId(UUID venueId);

    List<Event> findByOrganizerUsername(String organizerUsername);
}
