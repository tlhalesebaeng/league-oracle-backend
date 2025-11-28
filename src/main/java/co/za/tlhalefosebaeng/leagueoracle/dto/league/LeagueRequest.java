package co.za.tlhalefosebaeng.leagueoracle.dto.league;

import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class LeagueRequest {
    @NotBlank(message = "Name required! Please provide a league name")
    private String name;

    @Size(min = 2, message = "A league should have at least two (2) teams")
    @Size(max = 10, message = "A league can have a max of ten (10) teams")
    private List<Team> teams;

    // Helper method used to validate that the teams are unique
    public boolean validateTeam() {
        Set<String> teamNames = new HashSet<>();
        for(Team team : teams) {
            if(teamNames.contains(team.getName())) return false;
            teamNames.add(team.getName());
        }

        return true;
    }
}
