package joe.app.EventReservationApp.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import joe.app.EventReservationApp.DTO.VenueSummaryDTO;
import joe.app.EventReservationApp.Model.Venue;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    @Mapping(source = "id", target = "venueId")
    
    VenueSummaryDTO toVenueSummaryDTO(Venue venue);

    List<VenueSummaryDTO> toVenueSummaryDTOList(List<Venue> venues);
}
