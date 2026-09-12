package joe.app.EventReservationApp.Mapper;

import joe.app.EventReservationApp.DTO.ProfileDetailsDTO;
import joe.app.EventReservationApp.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "creationDate", target = "creationDate")
    ProfileDetailsDTO toProfileDetailsDTO(User user);
}
