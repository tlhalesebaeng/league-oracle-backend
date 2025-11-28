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
        // Get the user from the database using the user repository
        Optional<User> user = userRepo.findByEmail(email);

        // Return the user or throw the relevant exception if the user is not found i.e. the user object is null
        return user.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found! Please the email and try again"));
    }

    @Override
    public User addUser(SignupRequest requestDto) {
        // Check that the password and password confirm fields are the same otherwise throw the relevant exception
        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Passwords do not match! Please re-confirm your password");
        }

        // Instantiate a user instance and set the fields accordingly
        User newUser = new User();
        newUser.setFirstName(requestDto.getFirstName());
        newUser.setLastName(requestDto.getLastName());
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        // Save the user and return the saved user
        return userRepo.save(newUser);
    }

    @Override
    public User login(LoginRequest requestDto) {
        // Get the user from the database using user repository and confirm that the user exists
        Optional<User> user = userRepo.findByEmail(requestDto.getEmail());
        User savedUser = user.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Invalid credentials! Please check your email or password and try again"));


        // Confirm that the passwords are the same otherwise throw a relevant exception
        if(!passwordEncoder.matches(requestDto.getPassword(), savedUser.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid credentials! Please check your email or password and try again");
        }

        // When all checks are successful return the user
        return savedUser;
    }

    @Override
    public User checkAuth(String token) {
        try {
            String username = jwtService.getAllClaims(token).getSubject(); // Get the username from the token
            User user = this.getUserByEmail(username); // Load the user from the database using the user repository

            // Return the user if the provided token is valid
            if(jwtService.validateToken(token, new AppUserDetails(user))) return user;

            return null; // The token was not valid. This will help to confirm this on the controller
        } catch (Exception e) {
            // We catch any exception thrown here because we only want to tell the user whether they are logged in or not
            // We don't want to return an error to the user so the advice controller should not handle any exception thrown here
            return null;
        }
    }
}
