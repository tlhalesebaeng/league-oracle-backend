package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.mapper.TeamMapper;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/teams")
public class TeamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);
    private final TeamServiceInterface teamService;
    private final HttpServletRequest request;

    @PostMapping("")
    public ResponseEntity<TeamResponse> addTeam(@RequestParam Long leagueId, @Valid @RequestBody AddTeamRequest requestDto){
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Add Team: {} LeagueId {}", correlationId, leagueId);
        Team team = teamService.addTeam(leagueId, requestDto);
        TeamResponse responseDto = TeamMapper.toResponse(team);
        LOGGER.info("Add Team Successful: {} TeamId {}", correlationId, responseDto.getId());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<TeamResponse>> getAllLeagueTeams(@RequestParam Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Get All League Teams: {} LeagueId {}", correlationId, leagueId);
        List<Team> teams = teamService.getAllLeagueTeams(leagueId);
        List<TeamResponse> responseDto = TeamMapper.toResponseList(teams);
        LOGGER.info("Get All League Teams Successful: {} Leagues {}", correlationId, responseDto.size());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long teamId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Get Team: {} TeamId {}", correlationId, teamId);
        Team team = teamService.getTeam(teamId);
        TeamResponse responseDto = TeamMapper.toResponse(team);
        LOGGER.info("Get Team Successful: {} TeamId {}", correlationId, responseDto.getId()); // Log response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long teamId, @Valid @RequestBody UpdateTeamRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Update Team: {} TeamId {}", correlationId, teamId);
        Team team = teamService.updateTeam(teamId, requestDto);
        TeamResponse responseDto = TeamMapper.toResponse(team);
        LOGGER.info("Update Team Successful: {} TeamId {}", correlationId, responseDto.getId()); // Log response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long teamId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Delete Team: {} TeamId {}", correlationId, teamId);
        teamService.deleteTeam(teamId);
        LOGGER.info("Delete Team Successful: {}", correlationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
