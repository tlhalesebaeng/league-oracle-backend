package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/leagues")
public class LeagueController {
    public final LeagueServiceInterface leagueService;

    @PostMapping("")
    public ResponseEntity<League> createLeague() {
        League league = leagueService.createLeague(null);
        return ResponseEntity.ok(league);
    }
}
