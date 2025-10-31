package co.za.tlhalefosebaeng.leagueoracle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name required! Please provide a team name.")
    private String name;

    private int wins;
    private int draws;
    private int loses;
    private int goalsForward;
    private int goalsAgainst;

    @ManyToOne
    @JoinColumn(name = "league_id")
    @JsonIgnore
    private League league;

    // No need to cascade any operation because league is a parent of both team and fixture entities, it has relevant cascade types
    @OneToMany(mappedBy = "homeTeam")
    List<Fixture> homeFixtures;

    @OneToMany(mappedBy = "awayTeam")
    List<Fixture> awayFixtures;

    @OneToMany(mappedBy = "homeTeam")
    List<Result> homeResults;

    @OneToMany(mappedBy = "awayTeam")
    List<Result> awayResults;
}
