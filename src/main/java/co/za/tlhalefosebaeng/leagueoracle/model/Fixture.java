package co.za.tlhalefosebaeng.leagueoracle.model;

import co.za.tlhalefosebaeng.leagueoracle.utils.AppDates;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    public void setDate(String date) {
        this.date = AppDates.convertDate(date);
    }

    public String getDate() {
        // Avoid null pointer exception
        if(this.date == null) return null;

        // Convert the local date and time to string and return them in the correct format
        return AppDates.retrieveDate(this.date.toLocalDate(), this.date.toLocalTime());
    }

}
