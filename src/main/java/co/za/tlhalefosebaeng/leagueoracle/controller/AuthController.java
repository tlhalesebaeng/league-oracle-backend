package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.UserResponse;
import co.za.tlhalefosebaeng.leagueoracle.mapper.UserMapper;
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
        User user = userService.addUser(requestDto);
        String jwt = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_jwt", jwt));
        UserResponse responseDto = UserMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(true, responseDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest requestDto, HttpServletResponse response) {
        User user = userService.login(requestDto);
        String jwt = jwtService.generateToken(user);
        response.addCookie(cookieService.create("access_jwt", jwt));
        UserResponse responseDto = UserMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(true, responseDto));
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkAuth(@CookieValue("access_jwt") String jwt) {
        User user = userService.checkAuth(jwt);
        if(user == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(false, null));
        UserResponse responseDto = UserMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(true, responseDto));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        response.addCookie(cookieService.create("access_jwt", ""));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); // No need to return anything to the client
    }
}
