package co.za.tlhalefosebaeng.leagueoracle.dto.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LeagueResponse {
    // We don't need any validation on fields of this class since we are the one managing it's objects
    private String name;

    private List<TeamResponse> teams;
}
