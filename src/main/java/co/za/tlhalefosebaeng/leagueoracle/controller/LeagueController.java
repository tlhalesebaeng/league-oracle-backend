package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.mapper.LeagueMapper;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/leagues")
public class LeagueController {
    public final LeagueServiceInterface leagueService;

    @PostMapping("")
    public ResponseEntity<LeagueResponse> createLeague(@Valid @RequestBody LeagueRequest requestDto) {
        // Persist the league using the league service
        League league = leagueService.createLeague(requestDto);

        // Convert the league to a league response dto
        LeagueResponse responseDto = LeagueMapper.toResponse(league);

        // Send a 201 status code to the client and the league response dto as the body
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<LeagueResponse>> getAllLeagues(@RequestParam(required = false) String name) {
        // Get all leagues by the provided name using the league service
        List<League> leagues = leagueService.getAllLeagues(name);

        // Convert all the leagues to a list of league response dtos
        List<LeagueResponse> responseDto = LeagueMapper.toResponseList(leagues);

        // Send a 200 status code to the client and the list of league response dtos as the body
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<LeagueResponse>> getMyLeagues() {
        // Get all the leagues that the authenticated user has created using the service
        List<League> leagues = leagueService.getMyLeagues();

        // Convert all the leagues to a list of league response dtos
        List<LeagueResponse> responseDto = LeagueMapper.toResponseList(leagues);

        // Send a 200 status code to the client and the list of league response dtos as the body
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueResponse> getLeague(@PathVariable Long leagueId) {
        // Get the league that has the provided league id using the league service
        League league = leagueService.getLeague(leagueId);

        // Convert the league to a league response dto
        LeagueResponse responseDto = LeagueMapper.toResponse(league);

        // Send a 200 status code to the client and the league response dto as the body
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{leagueId}")
    public ResponseEntity<LeagueResponse> updateLeague(@PathVariable Long leagueId, @Valid @RequestBody LeagueRequest requestDto) {
        // Update the league that has the provided league id using the league service
        League league = leagueService.updateLeague(leagueId, requestDto);

        // Convert the league to a league response dto
        LeagueResponse responseDto = LeagueMapper.toResponse(league);

        // Send a 200 status code to the client and the league response dto as the body
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Object> deleteLeague(@PathVariable Long leagueId){
        // Delete the league that has the provided league id using the league service
        leagueService.deleteLeague(leagueId);

        // Send only a 200  status code to the client
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
