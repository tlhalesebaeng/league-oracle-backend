package co.za.tlhalefosebaeng.leagueoracle.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private UserResponse user;
}
