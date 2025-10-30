package co.za.tlhalefosebaeng.leagueoracle.dto.fixture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFixtureRequest {
    private String date;
    private String time;
    private String venue;
    private String field;
}
