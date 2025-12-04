package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.mapper.FixtureMapper;
import co.za.tlhalefosebaeng.leagueoracle.entity.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
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
@RequestMapping("${api.endpoint.prefix}/fixtures")
public class FixtureController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixtureController.class);
    private final FixtureServiceInterface fixtureService;

    @PostMapping("")
    public ResponseEntity<List<FixtureResponse>> createLeagueFixtures(@RequestAttribute("correlation-id") String correlationId, @RequestParam Long leagueId) {
        LOGGER.info("Attempting Create League Fixtures: {} LeagueId {}", correlationId, leagueId);
        List<Fixture> fixtures = fixtureService.generateLeagueFixtures(leagueId);
        List<FixtureResponse> responseDto = FixtureMapper.toResponseList(fixtures);
        LOGGER.info("Create League Fixtures Successful: {} LeagueId {} Fixtures {}", correlationId, leagueId, responseDto.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<FixtureResponse>> getAllLeagueFixtures(@RequestAttribute("correlation-id") String correlationId, @RequestParam Long leagueId) {
        LOGGER.info("Attempting Get All League Fixtures: {} LeagueId {}", correlationId, leagueId);
        List<Fixture> fixtures = fixtureService.getAllLeagueFixtures(leagueId);
        List<FixtureResponse> responseDto = FixtureMapper.toResponseList(fixtures);
        LOGGER.info("Get All League Fixtures Successful: {} LeagueId {} Fixtures {}", correlationId, leagueId, responseDto.size());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{fixtureId}")
    public ResponseEntity<FixtureResponse> getFixture(@RequestAttribute("correlation-id") String correlationId, @PathVariable Long fixtureId) {
        LOGGER.info("Attempting Get Fixture: {} FixtureId {}", correlationId, fixtureId);
        Fixture fixture = fixtureService.getFixture(fixtureId);
        FixtureResponse responseDto = FixtureMapper.toResponse(fixture);
        LOGGER.info("Get Fixture Successful: {} FixtureId {}", correlationId, responseDto.getId()); // Use response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FixtureResponse>> getUpcomingFixtures(@RequestAttribute("correlation-id") String correlationId, @RequestParam Integer month) {
        LOGGER.info("Attempting Get Upcoming Fixtures: {} Month {}", correlationId, month);
        List<Fixture> fixtures = fixtureService.getUpcomingFixtures(month);
        List<FixtureResponse> responseDto = FixtureMapper.toResponseList(fixtures);
        LOGGER.info("Get Upcoming Fixtures Successful: {} Month {} Fixtures {}", correlationId, month, responseDto.size());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{fixtureId}")
    public ResponseEntity<FixtureResponse> updateFixture(@RequestAttribute("correlation-id") String correlationId, @PathVariable Long fixtureId, @Valid @RequestBody UpdateFixtureRequest requestDto) {
        LOGGER.info("Attempting Update Fixture: {} FixtureId {}", correlationId, fixtureId);
        Fixture fixture = fixtureService.updateFixture(fixtureId, requestDto);
        FixtureResponse responseDto = FixtureMapper.toResponse(fixture);
        LOGGER.info("Update Fixture Successful: {} FixtureId {}", correlationId, responseDto.getId()); // Use response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{fixtureId}")
    public ResponseEntity<Object> deleteFixture(@RequestAttribute("correlation-id") String correlationId, @PathVariable Long fixtureId) {
        LOGGER.info("Attempting Delete Fixture: {} FixtureId {}", correlationId, fixtureId);
        fixtureService.deleteFixture(fixtureId);
        LOGGER.info("Delete Fixture Successful: {}", correlationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
