package co.za.tlhalefosebaeng.leagueoracle.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Missing field! All fields are required")
    private String email;

    @NotBlank(message = "Missing field! All fields are required")
    private String password;
}
