package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/teams")
public class TeamController {
    private final TeamServiceInterface teamService;
    private final LeagueServiceInterface leagueService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> addTeamToLeague(@RequestParam Long leagueId, @RequestBody AddTeamRequest team){
        // Add the team to the league corresponding to this leagueId
        League league = teamService.addTeamToLeague(leagueId, team);

        // Convert the league to a league response dto and return it
        return ResponseEntity.ok(new ApiResponse("success", leagueService.convertLeagueToDto(league)));
    }
}
