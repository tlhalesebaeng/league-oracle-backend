package co.za.tlhalefosebaeng.leagueoracle.dto.fixture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFixtureRequest {
    private Date date;
    private String time;
    private String venue;
    private String field;
}
