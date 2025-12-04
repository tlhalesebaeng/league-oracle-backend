package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.mapper.LeagueMapper;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
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
@RequestMapping("${api.endpoint.prefix}/leagues")
public class LeagueController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeagueController.class);
    public final LeagueServiceInterface leagueService;

    @PostMapping("")
    public ResponseEntity<LeagueResponse> createLeague(@RequestAttribute("correlation-id") String correlationId, @Valid @RequestBody LeagueRequest requestDto) {
        LOGGER.info("Attempting Create League: {}", correlationId);
        League league = leagueService.createLeague(requestDto);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        LOGGER.info("Create League Successful: {} LeagueId {}", correlationId, responseDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<LeagueResponse>> getAllLeagues(@RequestAttribute("correlation-id") String correlationId, @RequestParam(required = false) String name) {
        LOGGER.info("Attempting Get All Leagues: {} Name {}", correlationId, name);
        List<League> leagues = leagueService.getAllLeagues(name);
        List<LeagueResponse> responseDto = LeagueMapper.toResponseList(leagues);
        LOGGER.info("Get All Leagues Successful: {} Leagues {}", correlationId, responseDto.size());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<LeagueResponse>> getMyLeagues(@RequestAttribute("correlation-id") String correlationId) {
        LOGGER.info("Attempting Get My Leagues: {}", correlationId);
        List<League> leagues = leagueService.getMyLeagues();
        List<LeagueResponse> responseDto = LeagueMapper.toResponseList(leagues);
        LOGGER.info("Get My Leagues Successful: {} Leagues {}", correlationId, responseDto.size());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueResponse> getLeague(@RequestAttribute("correlation-id") String correlationId, @PathVariable Long leagueId) {
        LOGGER.info("Attempting Get League: {} LeagueId {}", correlationId, leagueId);
        League league = leagueService.getLeague(leagueId);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        LOGGER.info("Get League Successful: {} LeagueId {}", correlationId, responseDto.getId()); // Use response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/{leagueId}")
    public ResponseEntity<LeagueResponse> updateLeague(@RequestAttribute("correlation-id") String correlationId, @PathVariable Long leagueId, @Valid @RequestBody LeagueRequest requestDto) {
        LOGGER.info("Attempting Update League: {} LeagueId {}", correlationId, leagueId);
        League league = leagueService.updateLeague(leagueId, requestDto);
        LeagueResponse responseDto = LeagueMapper.toResponse(league);
        LOGGER.info("Update League Successful: {} LeagueId {}", correlationId, responseDto.getId()); // Use response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Object> deleteLeague(@RequestAttribute("correlation-id") String correlationId, @PathVariable Long leagueId){
        LOGGER.info("Attempting Delete League: {} LeagueId {}", correlationId, leagueId);
        leagueService.deleteLeague(leagueId);
        LOGGER.info("Delete League Successful: {}", correlationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
