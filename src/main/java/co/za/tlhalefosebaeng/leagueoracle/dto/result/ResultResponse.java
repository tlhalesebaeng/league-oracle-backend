package co.za.tlhalefosebaeng.leagueoracle.dto.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {
    private Long id;
    private String date;
    private String time;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;
}
