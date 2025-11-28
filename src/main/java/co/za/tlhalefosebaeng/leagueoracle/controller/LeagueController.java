package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.mapper.LeagueMapper;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
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
        League league = leagueService.createLeague(requestDto);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<LeagueResponse>> getAllLeagues(@RequestParam(required = false) String name) {
        List<League> leagues = leagueService.getAllLeagues(name);
        List<LeagueResponse> responseDto = LeagueMapper.toResponseList(leagues);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<LeagueResponse>> getMyLeagues() {
        List<League> leagues = leagueService.getMyLeagues();
        List<LeagueResponse> responseDto = LeagueMapper.toResponseList(leagues);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueResponse> getLeague(@PathVariable Long leagueId) {
        League league = leagueService.getLeague(leagueId);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{leagueId}")
    public ResponseEntity<LeagueResponse> updateLeague(@PathVariable Long leagueId, @Valid @RequestBody LeagueRequest requestDto) {
        League league = leagueService.updateLeague(leagueId, requestDto);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Object> deleteLeague(@PathVariable Long leagueId){
        leagueService.deleteLeague(leagueId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
