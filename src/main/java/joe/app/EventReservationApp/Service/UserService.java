package joe.app.EventReservationApp.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import joe.app.EventReservationApp.DTO.UserSummaryDTO;
import joe.app.EventReservationApp.Mapper.UserMapper;
import joe.app.EventReservationApp.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserSummaryDTO> getAllUsers() {
        List<UserSummaryDTO> response = userMapper.toSummaryDTOList(userRepository.findAll());
        return response;
    }

    public UserSummaryDTO getUserById(UUID userId) {
        UserSummaryDTO response = userMapper.toSummaryDTO(userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId)));
        return response;
    }

}
