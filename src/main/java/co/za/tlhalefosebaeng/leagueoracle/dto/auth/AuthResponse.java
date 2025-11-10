package co.za.tlhalefosebaeng.leagueoracle.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Boolean isAuth;
    private UserResponse user;

    public AuthResponse(UserResponse user) {
        this.user = user;
    }
}
