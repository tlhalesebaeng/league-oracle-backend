package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.mapper.ResultMapper;
import co.za.tlhalefosebaeng.leagueoracle.entity.Result;
import co.za.tlhalefosebaeng.leagueoracle.service.result.ResultServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("${api.endpoint.prefix}/results")
public class ResultController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultController.class);
    private final ResultServiceInterface resultService;
    private final HttpServletRequest request;

    @PostMapping("")
    public ResponseEntity<ResultResponse> addResult(@RequestParam Long fixtureId, @Valid @RequestBody ResultRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Add Fixture Result: {} FixtureId {}", correlationId, fixtureId);
        Result result = resultService.addResult(fixtureId, requestDto);
        ResultResponse responseDto = ResultMapper.toResponse(result);
        LOGGER.info("Add Fixture Result Successful: {} ResultId {}", correlationId, responseDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<ResultResponse>> getLeagueResults(@RequestParam Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Get All League Results: {} LeagueId {}", correlationId, leagueId);
        List<Result> results = resultService.getLeagueResults(leagueId);
        List<ResultResponse> responseDto = ResultMapper.toResponseList(results);
        LOGGER.info("Get All League Results Successful: {} Leagues {}", correlationId, responseDto.size());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("{resultId}")
    public ResponseEntity<ResultResponse> getResult(@PathVariable Long resultId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Get Result: {} ResultId {}", correlationId, resultId);
        Result result = resultService.getResult(resultId);
        ResultResponse responseDto = ResultMapper.toResponse(result);
        LOGGER.info("Get Result Successful: {} ResultId {}", correlationId, responseDto.getId()); // Use response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("{resultId}")
    public ResponseEntity<ResultResponse> updateResult(@PathVariable Long resultId, @Valid @RequestBody ResultRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Attempting Update Result: {} ResultId {}", correlationId, resultId);
        Result result = resultService.updateResult(resultId, requestDto);
        ResultResponse responseDto = ResultMapper.toResponse(result);
        LOGGER.info("Attempting Update Result: {} ResultId {}", correlationId, responseDto.getId()); // Use response id to confirm that the ids are similar
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
