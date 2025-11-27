package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.mapper.LeagueMapper;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
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

    // All the team endpoint should return the league because we will never query the team without needing league details
    @PostMapping("")
    public ResponseEntity<LeagueResponse> addTeam(@RequestParam Long leagueId, @RequestBody AddTeamRequest requestDto){
        // Add the team to the league that has the provided league id using the team service
        League league = teamService.addTeam(leagueId, requestDto);

        // Convert the league to a league response dto
        LeagueResponse responseDto = LeagueMapper.toResponse(league);

        // Send a 200 status code to the client and the league response dto as the body
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<LeagueResponse> updateTeam(@RequestParam Long leagueId, @PathVariable Long teamId, @Valid @RequestBody UpdateTeamRequest requestDto) {
        // Update the team that has the provided team using the team service
        League league = teamService.updateTeam(leagueId, teamId, requestDto);

        // Convert the league to a league response dto
        LeagueResponse responseDto = LeagueMapper.toResponse(league);

        // Send a 200 status code to the client and the league response dto as the body
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long teamId) {
        // Delete the team that has the provided team id using the team service
        teamService.deleteTeam(teamId);

        // Send only a 200 status code to the client
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
