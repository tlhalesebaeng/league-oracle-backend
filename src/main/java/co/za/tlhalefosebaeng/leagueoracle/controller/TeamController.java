package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.mapper.TeamMapper;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/teams")
public class TeamController {
    private final TeamServiceInterface teamService;

    @PostMapping("")
    public ResponseEntity<TeamResponse> addTeam(@RequestParam Long leagueId, @Valid @RequestBody AddTeamRequest requestDto){
        Team team = teamService.addTeam(leagueId, requestDto);
        TeamResponse responseDto = TeamMapper.toResponse(team);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<TeamResponse>> getAllLeagueTeams(@RequestParam Long leagueId) {
        List<Team> teams = teamService.getAllLeagueTeams(leagueId);
        List<TeamResponse> responseDto = TeamMapper.toResponseList(teams);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long teamId) {
        Team team = teamService.getTeam(teamId);
        TeamResponse responseDto = TeamMapper.toResponse(team);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(@RequestParam Long leagueId, @PathVariable Long teamId, @Valid @RequestBody UpdateTeamRequest requestDto) {
        Team team = teamService.updateTeam(teamId, requestDto);
        TeamResponse responseDto = TeamMapper.toResponse(team);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
