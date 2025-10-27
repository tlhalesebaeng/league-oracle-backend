package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> createLeague(@Valid @RequestBody LeagueRequest league) {
        LeagueResponse createdLeague = leagueService.createLeague(league);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", createdLeague));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllLeagues(@RequestParam(required = false) String name) {
        List<League> leagues = leagueService.getAllLeagues(name);
        return ResponseEntity.ok(new ApiResponse("success", leagues));
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<ApiResponse> getLeague(@PathVariable Long leagueId) {
        League league = leagueService.getLeague(leagueId);
        return ResponseEntity.ok(new ApiResponse("success", league));
    }

    @PatchMapping("/{leagueId}")
    public ResponseEntity<ApiResponse> updateLeague(@PathVariable Long leagueId, @RequestBody LeagueRequest league) {
        League updatedLeague = leagueService.updateLeague(leagueId, league);
        return ResponseEntity.ok(new ApiResponse("success", updatedLeague));
    }

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Object> deleteLeague(@PathVariable Long leagueId){
        leagueService.deleteLeague(leagueId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
