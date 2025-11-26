package co.za.tlhalefosebaeng.leagueoracle.response;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Boolean isAuth;
    private UserResponse user;
}
