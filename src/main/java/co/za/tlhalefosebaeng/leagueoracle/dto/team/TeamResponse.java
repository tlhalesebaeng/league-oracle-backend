package co.za.tlhalefosebaeng.leagueoracle.dto.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public void setPlayedGames() {
        this.playedGames = this.wins + this.draws + this.loses;
    }

    public void setPoints() {
        this.points = (this.wins * 3) + this.draws;
    }

    public void setGoalDifference() {
        this.goalDifference = this.goalsForward - this.goalsAgainst;
    }

}
