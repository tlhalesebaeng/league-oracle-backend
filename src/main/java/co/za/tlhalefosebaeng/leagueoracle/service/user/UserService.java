package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.UserResponse;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.IncorrectCredentialsException;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.PasswordsNotMatchingException;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse convertUserToDto(User user) {
        // Instantiate a new user instance
        UserResponse userResponse = new UserResponse();

        // Set the properties of the user response and return the user response instance
        userResponse.setFullName(user.getFullName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    @Override
    public User getUserByEmail(String email) {
        // Get the user from the database using the user repository
        Optional<User> user = userRepo.findByEmail(email);

        // Return the user or throw the relevant exception if the user is not found i.e. the user object is null
        return user.orElseThrow(() -> new ResourceNotFoundException("User not found! Please the email and try again"));
    }

    @Override
    public User addUser(SignupRequest user) {
        // Check that the password and password confirm fields are the same otherwise throw the relevant exception
        if(!user.getPassword().equals(user.getPasswordConfirm())){
            throw new PasswordsNotMatchingException("Passwords do not match! Please re-confirm your password");
        }

        // Instantiate a user instance and set the fields accordingly
        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user and return the saved user
        return userRepo.save(newUser);
    }

    @Override
    public User login(LoginRequest details) {
        // Get the user from the database using user repository and confirm that the user exists
        Optional<User> user = userRepo.findByEmail(details.getEmail());
        User savedUser = user.orElseThrow(() -> new IncorrectCredentialsException("Invalid credentials! Please check your email or password and try again"));


        // Confirm that the passwords are the same otherwise throw a relevant exception
        if(!passwordEncoder.matches(details.getPassword(), savedUser.getPassword())) {
            throw new IncorrectCredentialsException("Invalid credentials! Please check your email or password and try again");
        }

        // When all checks are successful return the user
        return savedUser;
    }
}
