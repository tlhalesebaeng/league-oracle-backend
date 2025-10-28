package co.za.tlhalefosebaeng.leagueoracle.dto.team;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddTeamRequest {
    @NotBlank(message = "Team name required! Please provide a team name")
    private String name;
}
