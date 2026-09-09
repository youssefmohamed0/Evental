package joe.app.EventReservationApp.Repository;

import joe.app.EventReservationApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}
