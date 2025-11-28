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
    public ResponseEntity<LeagueResponse> addTeam(@RequestParam Long leagueId, @Valid @RequestBody AddTeamRequest requestDto){
        League league = teamService.addTeam(leagueId, requestDto);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<LeagueResponse> updateTeam(@RequestParam Long leagueId, @PathVariable Long teamId, @Valid @RequestBody UpdateTeamRequest requestDto) {
        League league = teamService.updateTeam(leagueId, teamId, requestDto);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
