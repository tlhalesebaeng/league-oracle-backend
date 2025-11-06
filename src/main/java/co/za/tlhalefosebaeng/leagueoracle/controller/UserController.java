package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest user){
        // Sign the user up using the user service
        User newUser = userService.addUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", newUser));
    }
}
