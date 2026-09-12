package joe.app.EventReservationApp.Mapper;

import joe.app.EventReservationApp.DTO.CreateEventRequestDTO;
import joe.app.EventReservationApp.DTO.EventDetailDTO;
import joe.app.EventReservationApp.DTO.EventSummaryDTO;
import joe.app.EventReservationApp.DTO.SeatResponseDTO;
import joe.app.EventReservationApp.Model.Event;
import joe.app.EventReservationApp.Model.Seat;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "venue.id", target = "venueId")
    @Mapping(source = "venue.name", target = "venueName")
    @Mapping(source = "venue.stars", target = "venueStars")
    @Mapping(source = "startTime", target = "startTimestamp")
    @Mapping(target = "seatCapacity", expression = "java(calculateAvailable(event))")
    EventSummaryDTO toEventSummaryDTO(Event event);

    List<EventSummaryDTO> toEventSummaryDTOList(List<Event> events);

    default int calculateAvailable(Event event) {
        if (event.getSeats() == null)
            return 0;
        return (int) event.getSeatCapacity();
    }

    @Mapping(source = "event.venue.id", target = "venueId")
    @Mapping(source = "event.venue.name", target = "venueName")
    @Mapping(source = "event.venue.stars", target = "venueStars")
    @Mapping(source = "event.startTime", target = "startTimestamp")
    @Mapping(source = "event.endTime", target = "endTimestamp")
    @Mapping(source = "event.organizer.name", target = "organizerName")
    @Mapping(source = "availableSeats", target = "seatCapacity")
    EventDetailDTO toEventDetailDTO(Event event, long availableSeats);

    SeatResponseDTO toSeatResponseDTO(Seat seat);

    List<SeatResponseDTO> toSeatResponseDTOList(List<Seat> seat);

    @Mapping(source = "startTimestamp", target = "startTime")
    @Mapping(source = "endTimestamp", target = "endTime")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "seats", ignore = true)
    Event toEvent(CreateEventRequestDTO requestDTO);
}
