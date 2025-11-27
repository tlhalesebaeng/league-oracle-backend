package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
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

        // Convert the fixtures to fixture response dtos
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fixtureResponses);
    }

    @GetMapping("")
    public ResponseEntity<List<FixtureResponse>> getAllLeagueFixtures(@RequestParam Long leagueId) {
        List<Fixture> fixtures = fixtureService.getAllLeagueFixtures(leagueId);

        // Convert the fixtures to fixture response dtos
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.OK).body(fixtureResponses);
    }

    @GetMapping("/{fixtureId}")
    public ResponseEntity<FixtureResponse> getFixture(@PathVariable Long fixtureId) {
        // Get the fixture by the id from the database using the fixture service
        Fixture fixture = fixtureService.getFixture(fixtureId);

        // Convert the fixture to a fixture response and return it
        return ResponseEntity.status(HttpStatus.OK).body(fixtureService.convertFixtureToDto(fixture));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FixtureResponse>> getUpcomingFixtures(@RequestParam Integer month) {
        // Get upcoming fixtures that are scheduled during the provided month using the fixture service
        List<Fixture> fixtures = fixtureService.getUpcomingFixtures(month);

        // Convert the fixtures to fixture response dtos
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.OK).body(fixtureResponses);
    }

    @PatchMapping("/{fixtureId}")
    public ResponseEntity<FixtureResponse> updateFixture(@PathVariable Long fixtureId, @RequestBody UpdateFixtureRequest requestDto) {
        // Update the provided fixture fields using the fixture service
        Fixture fixture = fixtureService.updateFixture(fixtureId, requestDto);

        // Convert the fixture to a fixture response and return it
        return ResponseEntity.status(HttpStatus.OK).body( fixtureService.convertFixtureToDto(fixture));
    }

    @DeleteMapping("/{fixtureId}")
    public ResponseEntity<Object> deleteFixture(@PathVariable Long fixtureId) {
        // Delete the fixture using the fixture service
        fixtureService.deleteFixture(fixtureId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
