package co.za.tlhalefosebaeng.leagueoracle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams;

    @OneToMany(mappedBy = "league", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Fixture> fixtures;

    @OneToMany(mappedBy = "league", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Result> results;
}
