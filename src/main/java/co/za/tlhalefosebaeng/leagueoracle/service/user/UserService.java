package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.UserRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.jwt.JwtServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceInterface jwtService;
    private final HttpServletRequest request;

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        return user.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found! Please the email and try again"));
    }

    @Override
    public User addUser(SignupRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to save user: {}", correlationId);
        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Passwords do not match! Please re-confirm your password");
        }

        User user = new User();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        User dbUser = userRepo.save(user);
        LOGGER.info("Successfully saved user: {} UserId {}", correlationId, dbUser);
        return dbUser;
    }

    @Override
    public User login(LoginRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch user: {}", correlationId);
        Optional<User> user = userRepo.findByEmail(requestDto.getEmail());
        User savedUser = user.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Invalid credentials! Please check your email or password and try again"));

        if(!passwordEncoder.matches(requestDto.getPassword(), savedUser.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid credentials! Please check your email or password and try again");
        }

        LOGGER.info("Successfully fetched user: {}", correlationId);
        return savedUser;
    }

    @Override
    public User checkAuth(String token) {
        try {
            String correlationId = (String) request.getAttribute("correlation-id");
            LOGGER.info("Preparing check authentication: {}", correlationId);
            String username = jwtService.getAllClaims(token).getSubject();
            User user = this.getUserByEmail(username);
            if(jwtService.validateToken(token, new AppUserDetails(user))) {
                LOGGER.info("User is authenticated: {} UserId {}", correlationId, user.getId());
                return user;
            }

            LOGGER.info("User is authenticated: {} UserId {}", correlationId, user.getId());
            return null; // The token was not valid. This will help to confirm this on the controller
        } catch (Exception e) {
            // We catch any exception thrown here because we only want to tell the user whether they are logged in or not
            // We don't want to return an error to the user so the advice controller should not handle any exception thrown here
            return null;
        }
    }
}
