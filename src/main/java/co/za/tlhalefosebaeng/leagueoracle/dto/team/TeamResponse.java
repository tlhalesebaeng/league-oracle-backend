package co.za.tlhalefosebaeng.leagueoracle.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamResponse {
    private Long id;
    private String name;
    private int wins;
    private int draws;
    private int loses;
    private int goalsForward;
    private int goalsAgainst;
    private int playedGames;
    private int points;
    private int goalDifference;
}
