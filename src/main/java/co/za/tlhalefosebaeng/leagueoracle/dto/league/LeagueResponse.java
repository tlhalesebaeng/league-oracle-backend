package co.za.tlhalefosebaeng.leagueoracle.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueResponse {
    private Long id;
    private String name;
    private Long creator;
    private String createdAt;
}
