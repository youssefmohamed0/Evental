package joe.app.EventReservationApp.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import joe.app.EventReservationApp.DTO.CreateVenueDTO;
import joe.app.EventReservationApp.DTO.VenueSummaryDTO;
import joe.app.EventReservationApp.Exception.VenueNotFoundException;
import joe.app.EventReservationApp.Mapper.VenueMapper;
import joe.app.EventReservationApp.Model.Venue;
import joe.app.EventReservationApp.Repository.VenueRepository;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private VenueMapper venueMapper;

    public List<VenueSummaryDTO> getAllVenues() {
        return venueMapper.toVenueSummaryDTOList(venueRepository.findAll());
    }

    public VenueSummaryDTO getVenueById(UUID id) {
        return venueMapper.toVenueSummaryDTO(venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue not found with id: " + id)));
    }

    public VenueSummaryDTO createVenue(CreateVenueDTO venueDTO) {
        Venue venue = new Venue();
        venue.setName(venueDTO.getName());
        venue.setLocation(venueDTO.getLocation());
        venue.setStars(venueDTO.getStars());
        venue.setDescription(venueDTO.getDescription());
        return venueMapper.toVenueSummaryDTO(venueRepository.save(venue));
    }

    public VenueSummaryDTO updateVenue(UUID id, CreateVenueDTO venueDTO) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new VenueNotFoundException("Venue not found with id: " + id));
        venue.setName(venueDTO.getName());
        venue.setLocation(venueDTO.getLocation());
        venue.setStars(venueDTO.getStars());
        venue.setDescription(venueDTO.getDescription());
        return venueMapper.toVenueSummaryDTO(venueRepository.save(venue));
    }

    public void deleteVenue(UUID id) {
        venueRepository.deleteById(id);
    }

}
