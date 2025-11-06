package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserServiceInterface{
    private final UserRepository userRepo;

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepo.findByEmail(email);
        return user.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "User not found! Please the email and try again"));
    }

    @Override
    public User addUser(SignupRequest user) {
        // Instantiate a user instance and set the fields accordingly
        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        // Save the user and return the saved user
        return userRepo.save(newUser);
    }
}
