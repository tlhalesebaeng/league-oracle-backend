package co.za.tlhalefosebaeng.leagueoracle.dto.result;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultRequest {
    @NotNull(message = "Home team score required! Please provide home team score")
    private Integer homeTeamScore;

    @NotNull(message = "Away team score required! Please provide away team score")
    private Integer awayTeamScore;
}
