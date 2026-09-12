package joe.app.EventReservationApp.Service;

import joe.app.EventReservationApp.DTO.ProfileDetailsDTO;
import joe.app.EventReservationApp.DTO.UpdateProfileDTO;
import joe.app.EventReservationApp.Mapper.ProfileMapper;
import joe.app.EventReservationApp.Model.User;
import joe.app.EventReservationApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileMapper profileMapper;

    public ProfileDetailsDTO getProfileDetails(Authentication authentication) {
        String username = authentication.getName();
        return profileMapper.toProfileDetailsDTO(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username)));
    }

    public ProfileDetailsDTO updateProfileDetails(Authentication authentication, UpdateProfileDTO request) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));
        if(request.getName() != null) user.setName(request.getName());
        if(request.getEmail() != null) user.setEmail(request.getEmail());
        if(request.getUsername() != null) user.setUsername(request.getUsername());
        if(request.getPassword() != null) user.setPassword(request.getPassword());
        return profileMapper.toProfileDetailsDTO(userRepository.save(user));
    }
}
