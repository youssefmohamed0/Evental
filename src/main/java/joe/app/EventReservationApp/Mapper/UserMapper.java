package joe.app.EventReservationApp.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import joe.app.EventReservationApp.DTO.UserSummaryDTO;
import joe.app.EventReservationApp.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role", target = "role")
    public UserSummaryDTO toSummaryDTO(User user);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "role", target = "role")
    public List<UserSummaryDTO> toSummaryDTOList(List<User> users);

}
