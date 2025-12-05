package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.UserRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.jwt.JwtServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceInterface jwtService;

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        return user.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found! Please the email and try again"));
    }

    @Override
    public User addUser(SignupRequest requestDto) {
        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Passwords do not match! Please re-confirm your password");
        }

        User newUser = new User();
        newUser.setFirstName(requestDto.getFirstName());
        newUser.setLastName(requestDto.getLastName());
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        return userRepo.save(newUser);
    }

    @Override
    public User login(LoginRequest requestDto) {
        Optional<User> user = userRepo.findByEmail(requestDto.getEmail());
        User savedUser = user.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Invalid credentials! Please check your email or password and try again"));

        if(!passwordEncoder.matches(requestDto.getPassword(), savedUser.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid credentials! Please check your email or password and try again");
        }

        return savedUser;
    }

    @Override
    public User checkAuth(String token) {
        try {
            String username = jwtService.getAllClaims(token).getSubject();
            User user = this.getUserByEmail(username);
            if(jwtService.validateToken(token, new AppUserDetails(user))) return user;

            return null; // The token was not valid. This will help to confirm this on the controller
        } catch (Exception e) {
            // We catch any exception thrown here because we only want to tell the user whether they are logged in or not
            // We don't want to return an error to the user so the advice controller should not handle any exception thrown here
            return null;
        }
    }
}
