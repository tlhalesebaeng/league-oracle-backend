package co.za.tlhalefosebaeng.leagueoracle.dto.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixtureResponse {
    private Long id;
    private String date;
    private String venue;
    private String field;
    private String time;
    private String name; // The name of the league this fixture belongs to
    private TeamResponse homeTeam;
    private TeamResponse awayTeam;

    public void setDate(String date) {
        // If the date is not provided, set the date property to "TBC". This should be
        // the default, but we cannot persist this on the database
        if(date == null) {
            this.date = "TBC";
            return;
        }

        // Extract the date from the given date string
        String[] dateTimeDetails = date.split(",");

        // Set the date property to the extracted date details
        this.date = dateTimeDetails[0];
    }

    public void setTime(String date) {
        // If the date is not provided, set the time property to "TBC". This should be
        // the default, but we cannot persist this on the database
        if(date == null) {
            this.time = "TBC";
            return;
        }

        // Extract time date from the given date string
        String[] dateTimeDetails = date.split(",");

        // Set the time property to the extracted time details
        this.time = dateTimeDetails[1];
    }
}
