package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.response.AuthResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.service.jwt.JwtServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.cookie.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.prefix}/auth")
public class AuthController {
    private final UserServiceInterface userService;
    private final JwtServiceInterface jwtService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest requestDto, HttpServletResponse response){
        // Sign the user up using the user service
        User newUser = userService.addUser(requestDto);

        String jwt = jwtService.generateToken(newUser); // Generate the token using the jwt service
        response.addCookie(cookieService.create("access_jwt", jwt)); // Create a cookie using the cookie service and add it to the response

        // Convert the user object to a user response dto and return it as part of the api response
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(true, userService.convertUserToDto(newUser)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest requestDto, HttpServletResponse response) {
        // Get the user from the database using the user service
        User user = userService.login(requestDto);

        // Generate the token and set it as a cookie in the response
        String jwt = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_jwt", jwt));

        // Convert the user object to a user response dto and return it as part of the api response
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(true, userService.convertUserToDto(user)));
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkAuth(@CookieValue("access_jwt") String jwt) {
        User user = userService.checkAuth(jwt); // Get the user using the user service

        // When the user is null return isAuth as false and the user as null
        if(user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(false, null));

        // When all checks are valid convert the user object to a user response dto and return it as part of the api response
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, userService.convertUserToDto(user)));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        // Add an empty token as a cookie to the response
        response.addCookie(cookieService.create("access_jwt", ""));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); // No need to return anything to the user
    }
}
