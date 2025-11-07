package co.za.tlhalefosebaeng.leagueoracle.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private String fullName;
    private String lastName;
    private String email;
}
