package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get the user from the database using the user repo. We use the user repo directly here to avoid a dependency cycle
        User user = userRepo
                .findByEmail(username)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST ,"User not found! Please check your email and try again"));

        // Create an instance of app user details, assign the user field using the constructor and return it
        return new AppUserDetails(user);
    }

    // Method to get the details of the currently logged-in user
    public UserDetails getPrincipal() {
        // Get the principal user from the security context and return it
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) auth.getPrincipal();
    }
}
