package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/fixtures")
public class FixtureController {
    private final FixtureServiceInterface fixtureService;

    @PostMapping("")
    public ResponseEntity<List<FixtureResponse>> createLeagueFixtures(@RequestParam Long leagueId) {
        List<Fixture> fixtures = fixtureService.generateLeagueFixtures(leagueId);
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fixtureResponses);
    }

    @GetMapping("")
    public ResponseEntity<List<FixtureResponse>> getAllLeagueFixtures(@RequestParam Long leagueId) {
        List<Fixture> fixtures = fixtureService.getAllLeagueFixtures(leagueId);
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.OK).body(fixtureResponses);
    }

    @GetMapping("/{fixtureId}")
    public ResponseEntity<FixtureResponse> getFixture(@PathVariable Long fixtureId) {
        Fixture fixture = fixtureService.getFixture(fixtureId);
        return ResponseEntity.status(HttpStatus.OK).body(fixtureService.convertFixtureToDto(fixture));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FixtureResponse>> getUpcomingFixtures(@RequestParam Integer month) {
        List<Fixture> fixtures = fixtureService.getUpcomingFixtures(month);
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }
        return ResponseEntity.status(HttpStatus.OK).body(fixtureResponses);
    }

    @PatchMapping("/{fixtureId}")
    public ResponseEntity<FixtureResponse> updateFixture(@PathVariable Long fixtureId, @Valid @RequestBody UpdateFixtureRequest requestDto) {
        Fixture fixture = fixtureService.updateFixture(fixtureId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body( fixtureService.convertFixtureToDto(fixture));
    }

    @DeleteMapping("/{fixtureId}")
    public ResponseEntity<Object> deleteFixture(@PathVariable Long fixtureId) {
        fixtureService.deleteFixture(fixtureId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
