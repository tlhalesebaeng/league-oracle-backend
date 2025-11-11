package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/teams")
public class TeamController {
    private final TeamServiceInterface teamService;
    private final LeagueServiceInterface leagueService;

    // All the team endpoint should return the league because we will never query the team without needing league details
    @PostMapping("")
    public ResponseEntity<LeagueResponse> addTeam(@RequestParam Long leagueId, @RequestBody AddTeamRequest team){
        // Add the team to the league corresponding to this leagueId
        League league = teamService.addTeam(leagueId, team);

        // Convert the league to a league response dto
        LeagueResponse leagueResponse = leagueService.convertLeagueToDto(league, teamService::convertTeamToDto);

        // Convert the league to a league response dto and return it
        return ResponseEntity.status(HttpStatus.OK).body(leagueResponse);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<LeagueResponse> updateTeam(
            @RequestParam Long leagueId,
            @PathVariable Long teamId,
            @Valid @RequestBody UpdateTeamRequest team
    ) {
        // Update the league team
        League league = teamService.updateTeam(leagueId, teamId, team);

        // Convert the league to a league response dto
        LeagueResponse leagueResponse = leagueService.convertLeagueToDto(league, teamService::convertTeamToDto);

        // Convert the league to a league response dto and return it
        return ResponseEntity.status(HttpStatus.OK).body(leagueResponse);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> addTeamToLeague(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
