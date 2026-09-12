package joe.app.EventReservationApp.Mapper;

import java.util.List;
import java.util.stream.Collectors; // Add this import

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import joe.app.EventReservationApp.DTO.CreateReservationResponseDTO;
import joe.app.EventReservationApp.DTO.ReservationDetailDTO;
import joe.app.EventReservationApp.DTO.ReservationSummaryDTO;
import joe.app.EventReservationApp.Model.Reservation;
import joe.app.EventReservationApp.Model.ReservationItem; // Add this import

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "totalPrice", target = "totalPrice")
    CreateReservationResponseDTO toCreateReservationResponseDTO(Reservation reservation);

    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "totalPrice", target = "totalPrice")
    ReservationSummaryDTO toReservationSummaryDTO(Reservation reservation);

    List<ReservationSummaryDTO> toReservationSummaryDTOList(List<Reservation> reservations);

    @Mapping(source = "id", target = "reservationId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "event.startTime", target = "eventStartTime")
    @Mapping(source = "event.endTime", target = "eventEndTime")
    @Mapping(source = "event.venue.name", target = "venueName")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "totalPrice", target = "totalPrice")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "items", target = "seatsNumbers")
    ReservationDetailDTO toReservationDetailDTO(Reservation reservation);

    // this default method so MapStruct knows how to convert the list of items
    // to a list of strings

    default List<String> mapItemsToSeatNumbers(List<ReservationItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(item -> item.getSeat().getSeatNumber())
                .collect(Collectors.toList());
    }
}