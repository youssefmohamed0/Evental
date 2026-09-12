package joe.app.EventReservationApp.Controller;

import joe.app.EventReservationApp.DTO.ProfileDetailsDTO;
import joe.app.EventReservationApp.DTO.UpdateProfileDTO;
import joe.app.EventReservationApp.Service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<ProfileDetailsDTO> getProfileDetails(Authentication authentication) {
        return ResponseEntity.ok(profileService.getProfileDetails(authentication));
    }

    @PutMapping("")
    public ResponseEntity<ProfileDetailsDTO> updateProfileDetails(Authentication authentication,
            @RequestBody UpdateProfileDTO updateProfileDTO) {
        return ResponseEntity.ok(profileService.updateProfileDetails(authentication, updateProfileDTO));
    }
}
