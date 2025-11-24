package co.za.tlhalefosebaeng.leagueoracle.dto.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {
    private Long id;
    private String date;
    private String time;
    private String name; // The name of the league this result belongs to
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;

    // Custom setters for the date and time fields

    public void setDate(LocalDate date) {
        if(date != null) this.date = date.toString();
        else this.date = "TBC";
    }

    public void setTime(LocalTime time) {
        if(time != null) this.time = time.toString();
        else this.time = "TBC";
    }
}
