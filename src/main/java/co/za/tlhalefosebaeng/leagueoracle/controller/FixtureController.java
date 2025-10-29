package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
