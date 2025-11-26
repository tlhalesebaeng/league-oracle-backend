package co.za.tlhalefosebaeng.leagueoracle.model;

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
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Integer homeTeamScore;
    private Integer awayTeamScore;

    @ManyToOne
    @JoinColumn(name = "home_team")
    @JsonIgnore
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team")
    @JsonIgnore
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "league_id")
    @JsonIgnore
    private League league;
}
