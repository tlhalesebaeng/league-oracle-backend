package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/leagues")
public class LeagueController {
    public final LeagueServiceInterface leagueService;
    public final TeamServiceInterface teamService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> createLeague(@Valid @RequestBody LeagueRequest league) {
        // Persist the league on the database and receive the saved league
        League createdLeague = leagueService.createLeague(league);

        // Convert the league to a league response dto
        LeagueResponse leagueResponse = leagueService.convertLeagueToDto(createdLeague, teamService::convertTeamToDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", leagueResponse));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllLeagues(@RequestParam(required = false) String name) {
        // Get all leagues by name from the league service
        List<League> leagues = leagueService.getAllLeagues(name);

        // Convert all the leagues to the league response dtos and return them as part of the response entity
        List<LeagueResponse> leagueResponses = new ArrayList<>();
        for(League league : leagues) {
            leagueResponses.add(leagueService.convertLeagueToDto(league, teamService::convertTeamToDto));
        }

        return ResponseEntity.ok(new ApiResponse("success", leagueResponses));
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<ApiResponse> getLeague(@PathVariable Long leagueId) {
        // Get the league by id using the league service
        League league = leagueService.getLeague(leagueId);

        // Convert the league to a league response dto
        LeagueResponse leagueResponse = leagueService.convertLeagueToDto(league, teamService::convertTeamToDto);

        // Convert the league to a league response dto and return it as part of the response entity
        return ResponseEntity.ok(new ApiResponse("success", leagueResponse));
    }

    @PatchMapping("/{leagueId}")
    public ResponseEntity<ApiResponse> updateLeague(@PathVariable Long leagueId, @RequestBody LeagueRequest league) {
        // Update the league and get the newly updated league
        League updatedLeague = leagueService.updateLeague(leagueId, league);

        // Convert the league to a league response dto
        LeagueResponse leagueResponse = leagueService.convertLeagueToDto(updatedLeague, teamService::convertTeamToDto);

        return ResponseEntity.ok(new ApiResponse("success", leagueResponse));
    }

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Object> deleteLeague(@PathVariable Long leagueId){
        leagueService.deleteLeague(leagueId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
