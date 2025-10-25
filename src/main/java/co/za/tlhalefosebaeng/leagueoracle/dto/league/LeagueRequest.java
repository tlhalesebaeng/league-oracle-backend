package co.za.tlhalefosebaeng.leagueoracle.dto.league;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeagueRequest {
    @NotBlank(message = "Name required! Please provide a league name")
    private String name;
}
