package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;
import co.za.tlhalefosebaeng.leagueoracle.service.result.ResultServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/results")
public class ResultController {
    private final ResultServiceInterface resultService;

    @PostMapping("")
    public ResponseEntity<ResultResponse> addResult(@RequestParam Long fixtureId, @Valid @RequestBody ResultRequest requestDto) {
        Result result = resultService.addResult(fixtureId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.convertResultToDto(result));
    }

    @GetMapping("")
    public ResponseEntity<List<ResultResponse>> getLeagueResults(@RequestParam Long leagueId) {
        List<Result> results = resultService.getLeagueResults(leagueId);
        List<ResultResponse> convertedResults = new ArrayList<>();
        for(Result result : results) {
            convertedResults.add(resultService.convertResultToDto(result));
        }

        return ResponseEntity.status(HttpStatus.OK).body(convertedResults);
    }

    @GetMapping("{resultId}")
    public ResponseEntity<ResultResponse> getResult(@PathVariable Long resultId) {
        Result result = resultService.getResult(resultId);
        return ResponseEntity.status(HttpStatus.OK).body(resultService.convertResultToDto(result));
    }

    @PatchMapping("{resultId}")
    public ResponseEntity<ResultResponse> updateResult(@PathVariable Long resultId, @Valid @RequestBody ResultRequest requestDto) {
        Result result = resultService.updateResult(resultId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(resultService.convertResultToDto(result));
    }
}
