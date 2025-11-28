package co.za.tlhalefosebaeng.leagueoracle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Fixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;
    private String venue = "TBC";
    private String field = "TBC";

    @ManyToOne
    @JoinColumn(name = "league_id")
    @JsonIgnore
    private League league;

    @ManyToOne
    @JoinColumn(name = "home_team")
    @JsonIgnore
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team")
    @JsonIgnore
    private Team awayTeam;

    // Custom setters for the date and time fields

    public void setDate(String date) {
        // Dates that have a "TBC" value are also represented as null on the database
        if(date.equals("TBC")) {
            this.date = null;
            return;
        }

        // Set the date accordingly. The expected format of the date is yyyy-mm-dd
        String[] details = date.split("-");
        this.date = LocalDate.of(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));;
    }

    public void setTime(String time) {
        // Times that have a "TBC" value are also represented as null on the database
        if(time.equals("TBC")) {
            this.time = null;
            return;
        }

        // Set the time accordingly. The expected format of the time is HH:MM
        String[] details = time.split(":");
        this.time = LocalTime.of(Integer.parseInt(details[0]), Integer.parseInt(details[1]));
    }
}
