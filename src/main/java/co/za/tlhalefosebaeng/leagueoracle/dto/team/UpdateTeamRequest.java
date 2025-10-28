package co.za.tlhalefosebaeng.leagueoracle.dto.team;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTeamRequest {
    @Pattern(regexp = "^(?!\\s*$).+", message = "Invalid name! Please provide a non empty name")
    private String name;

    @PositiveOrZero(message = "Invalid wins! Please provide a positive value for wins")
    private int wins;

    @PositiveOrZero(message = "Invalid draws! Please provide a positive value for draws")
    private int draws;

    @PositiveOrZero(message = "Invalid loses! Please provide a positive value for loses")
    private int loses;

    @PositiveOrZero(message = "Invalid goals forward! Please provide a positive value for goals forward")
    private int goalsForward;

    @PositiveOrZero(message = "Invalid goals against! Please provide a positive value for goals against")
    private int goalsAgainst;
}
