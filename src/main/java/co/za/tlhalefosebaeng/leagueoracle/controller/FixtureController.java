package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> createLeagueFixtures(@RequestParam Long leagueId) {
        List<Fixture> fixtures = fixtureService.generateLeagueFixtures(leagueId);

        // Convert the fixtures to fixture response dtos
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", fixtureResponses));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllLeagueFixtures(@RequestParam Long leagueId) {
        List<Fixture> fixtures = fixtureService.getAllLeagueFixtures(leagueId);

        // Convert the fixtures to fixture response dtos
        List<FixtureResponse> fixtureResponses = new ArrayList<>();
        for(Fixture fixture : fixtures) {
            fixtureResponses.add(fixtureService.convertFixtureToDto(fixture));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", fixtureResponses));
    }

    @GetMapping("/{fixtureId}")
    public ResponseEntity<ApiResponse> getFixture(@PathVariable Long fixtureId) {
        // Get the fixture by the id from the database using the league service
        Fixture fixture = fixtureService.getLeagueById(fixtureId);

        // Convert the fixture to a fixture response and return it
        return ResponseEntity.ok(new ApiResponse("success", fixtureService.convertFixtureToDto(fixture)));
    }
}
