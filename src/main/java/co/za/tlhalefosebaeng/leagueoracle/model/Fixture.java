package co.za.tlhalefosebaeng.leagueoracle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private LocalDateTime date;
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

    // The provided date should be in the format yyyy-mm-dd,hh:mm to simplify data extraction
    public void setDate(String date) {
        // Avoid null pointer exception. There is no need to set the date to null as it will be null by default
        if(date == null) return;

        // Extract the date and time details from the given string
        String[] dateTimeDetails = date.split(",");
        String[] dateDetails = dateTimeDetails[0].split("-");
        String[] timeDetails = dateTimeDetails[1].split(":");

        // Set the local date using the extracted date details
        LocalDate localDate = LocalDate.of(
                Integer.parseInt(dateDetails[0]),
                Integer.parseInt(dateDetails[1]),
                Integer.parseInt(dateDetails[2])
        );

        // Set the local time using the extracted time details
        LocalTime localTime = LocalTime.of(
                Integer.parseInt(timeDetails[0]),
                Integer.parseInt(timeDetails[1])
        );

        // Set the local date and time property using the local date and time
        this.date = LocalDateTime.of(localDate, localTime);
    }

    // The returned date is in the format yyyy-mm-dd,hh:mm but the fixture response formats the
    // date and time fields so that it can be easier for clients to read
    public String getDate() {
        // Avoid null pointer exception
        if(this.date == null) return null;

        // Get the local date and time from the date property respectively
        LocalDate localDate = this.date.toLocalDate();
        LocalTime localTime = this.date.toLocalTime();

        // Convert the local date and time to string and return them in the correct format
        return localDate.toString() + "," + localTime.toString();
    }

}
