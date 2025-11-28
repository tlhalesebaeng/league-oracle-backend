package co.za.tlhalefosebaeng.leagueoracle.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTeamRequest {
    @NotBlank(message = "Invalid name! Please provide a valid name")
    private String name;
}
