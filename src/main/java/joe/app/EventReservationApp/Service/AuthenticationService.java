package joe.app.EventReservationApp.Service;

import jakarta.transaction.Transactional;
import joe.app.EventReservationApp.DTO.AuthResponseDTO;
import joe.app.EventReservationApp.DTO.LoginRequestDTO;
import joe.app.EventReservationApp.DTO.SignupRequestDTO;
import joe.app.EventReservationApp.DTO.RefreshResponseDTO;
import joe.app.EventReservationApp.Enum.Role;
import joe.app.EventReservationApp.Exception.TokenRefreshException;
import joe.app.EventReservationApp.Exception.UserCreationConflictException;
import joe.app.EventReservationApp.Model.RefreshToken;
import joe.app.EventReservationApp.Model.User;
import joe.app.EventReservationApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Transactional
    public AuthResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        String username = signupRequestDTO.getUsername() != null ? signupRequestDTO.getUsername().trim() : null;
        String email = signupRequestDTO.getEmail() != null ? signupRequestDTO.getEmail().trim() : null;
        Role role = Role.valueOf(signupRequestDTO.getRole() != null ? signupRequestDTO.getRole().trim() : null);
        if (userRepository.existsByUsername(username)) {
            throw new UserCreationConflictException("Username Already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserCreationConflictException("Email Already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        newUser.setRole(role);

        String jwt = jwtService.generateToken(newUser.getUsername(), newUser.getRole());
        User savedUser = userRepository.save(newUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setAccessToken(jwt);
        authResponseDTO.setRefreshToken(refreshToken.getToken());
        authResponseDTO.setUsername(savedUser.getUsername());
        authResponseDTO.setRole(savedUser.getRole().toString());

        return authResponseDTO;
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername() != null ? loginRequestDTO.getUsername().trim() : null;
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                loginRequestDTO.getPassword()));
        User loadedUser = userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        String jwt = jwtService.generateToken(loadedUser.getUsername(), loadedUser.getRole());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loadedUser);

        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setAccessToken(jwt);
        authResponseDTO.setRefreshToken(refreshToken.getToken());
        authResponseDTO.setUsername(loadedUser.getUsername());
        authResponseDTO.setRole(loadedUser.getRole().toString());

        return authResponseDTO;
    }

    @Transactional
    public RefreshResponseDTO refreshToken(String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername(), user.getRole());
                    return new RefreshResponseDTO(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
    }

    @Transactional
    public void logout(String requestRefreshToken) {
        refreshTokenService.findByToken(requestRefreshToken)
                .map(RefreshToken::getUser)
                .ifPresent(user -> refreshTokenService.deleteByUserId(user.getId()));
    }
}
