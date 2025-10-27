package co.za.tlhalefosebaeng.leagueoracle.dto.league;

import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LeagueRequest {
    @NotBlank(message = "Name required! Please provide a league name")
    private String name;

    @Size(min = 2, message = "A league should have at least two (2) teams")
    @Size(max = 10, message = "A league can have a max of ten (10) teams")
    private List<Team> teams;
}
