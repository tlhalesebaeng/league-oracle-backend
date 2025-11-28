package co.za.tlhalefosebaeng.leagueoracle.dto.fixture;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFixtureRequest {
    @Pattern(regexp = "^(?:19|20)\\d\\d-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])$", message = "Invalid date provided! Please check your date and check again")
    private String date;

    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "Invalid date provided! Please check your date and check again")
    private String time;

    private String venue;
    private String field;
}
