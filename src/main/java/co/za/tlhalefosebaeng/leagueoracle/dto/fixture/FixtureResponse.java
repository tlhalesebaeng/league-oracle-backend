package co.za.tlhalefosebaeng.leagueoracle.dto.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixtureResponse {
    private Long id;
    private Date date;
    private String venue;
    private String field;
    private String time;
    private String formattedDate;
    private String name; // The name of the league this fixture belongs to
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;
}
