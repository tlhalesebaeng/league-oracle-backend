package co.za.tlhalefosebaeng.leagueoracle.dto.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixtureResponse {
    private Long id;
    private Long league;
    private String date;
    private String venue;
    private String field;
    private String time;
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
