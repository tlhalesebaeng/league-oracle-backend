package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.UserResponse;
import co.za.tlhalefosebaeng.leagueoracle.mapper.UserMapper;
import co.za.tlhalefosebaeng.leagueoracle.response.AuthResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;
import co.za.tlhalefosebaeng.leagueoracle.service.jwt.JwtServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.cookie.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.prefix}/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final UserServiceInterface userService;
    private final JwtServiceInterface jwtService;
    private final CookieService cookieService;
    private final HttpServletRequest request;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest requestDto, HttpServletResponse response){
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Signup: {}", correlationId);
        User user = userService.addUser(requestDto);
        String jwt = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_jwt", jwt));
        UserResponse responseDto = UserMapper.toResponse(user);
        LOGGER.info("Signup Successful: {} UserId {}", correlationId, responseDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(true, responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest requestDto, HttpServletResponse response) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Login: {}", correlationId);
        User user = userService.login(requestDto);
        String jwt = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_jwt", jwt));
        UserResponse responseDto = UserMapper.toResponse(user);
        LOGGER.info("Login Successful: {} UserId {}", correlationId, responseDto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, responseDto));
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkAuth(@CookieValue("access_jwt") String jwt) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Auth Check: {}", correlationId);
        User user = userService.checkAuth(jwt);
        if(user == null) {
            LOGGER.info("Auth Check Successful, Not Authenticated: {}", correlationId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(false, null));
        }
        UserResponse responseDto = UserMapper.toResponse(user);
        LOGGER.info("Auth Check Successful, Authenticated: {} UserId {}", correlationId, responseDto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, responseDto));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Logout: {}", correlationId);
        response.addCookie(cookieService.create("access_jwt", ""));
        LOGGER.info("Logout Successful: {}", correlationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); // No need to return anything to the client
    }
}
