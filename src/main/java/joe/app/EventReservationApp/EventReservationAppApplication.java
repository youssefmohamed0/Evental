package joe.app.EventReservationApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventReservationAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventReservationAppApplication.class, args);
	}

}
