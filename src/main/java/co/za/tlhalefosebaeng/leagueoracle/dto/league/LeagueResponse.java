package co.za.tlhalefosebaeng.leagueoracle.dto.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueResponse {
    // We don't need any validation on fields of this class since we are the one managing it's objects
    private Long id;
    private String name;
    private Long creator;
    private List<TeamResponse> teams;
}
