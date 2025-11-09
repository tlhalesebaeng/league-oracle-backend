package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
import co.za.tlhalefosebaeng.leagueoracle.service.jwt.JwtServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.cookie.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.prefix}/auth")
public class UserController {
    private final UserServiceInterface userService;
    private final JwtServiceInterface jwtService;
    private final CookieService cookieService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest user, HttpServletResponse response){
        // Sign the user up using the user service
        User newUser = userService.addUser(user);

        String jwt = jwtService.generateToken(newUser); // Generate the token using the jwt service
        response.addCookie(cookieService.create("access_jwt", jwt)); // Create a cookie using the cookie service and add it to the response

        // Convert the user object to a user response dto and return it as part of the api response
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", userService.convertUserToDto(newUser)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest details, HttpServletResponse response) {
        // Get the user from the database using the user service
        User user = userService.login(details);

        // Generate the token and set it as a cookie in the response
        String jwt = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_jwt", jwt));

        // Convert the user object to a user response dto and return it as part of the api response
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", userService.convertUserToDto(user)));
    }
}
