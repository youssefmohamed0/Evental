package joe.app.EventReservationApp.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token_hash")
    private String token;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiration_date")
    private Date expirationDate;
}
