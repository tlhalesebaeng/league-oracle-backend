package co.za.tlhalefosebaeng.leagueoracle.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultRequest {
    private Integer homeTeamScore;
    private Integer awayTeamScore;
}
