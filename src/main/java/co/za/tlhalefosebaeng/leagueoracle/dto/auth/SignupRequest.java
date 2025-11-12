package co.za.tlhalefosebaeng.leagueoracle.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "First name required! Please provide your first name")
    private String firstName;

    @NotBlank(message = "Last name required! Please provide your last name")
    private String lastName;

    @NotBlank(message = "Email required! Please provide a valid email address")
    @Email(message = "Invalid email! Please provide a valid email", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotBlank(message = "Password required! Please provide a password")
    private String password;

    @NotBlank(message = "Password confirmation required! Please confirm your password")
    private String passwordConfirm;
}
