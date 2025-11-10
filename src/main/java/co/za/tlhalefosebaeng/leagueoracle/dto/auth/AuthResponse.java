package co.za.tlhalefosebaeng.leagueoracle.dto.auth;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean isAuth;
    private UserResponse user;

    public AuthResponse(UserResponse user) {
        this.user = user;
    }
}
