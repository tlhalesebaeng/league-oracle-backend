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
    public ResponseEntity<ResultResponse> addResult(@RequestParam Long fixtureId, @Valid @RequestBody ResultRequest resultRequest) {
        // Add results of a fixture to the database using the result service
        Result result = resultService.addResult(fixtureId, resultRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.convertResultToDto(result));
    }

    @GetMapping("")
    public ResponseEntity<List<ResultResponse>> getLeagueResults(@RequestParam Long leagueId) {
        // Get a list of all the results that belong to the league with the given league id
        List<Result> results = resultService.getLeagueResults(leagueId);

        // Convert the results into result response dto and return the new list
        List<ResultResponse> convertedResults = new ArrayList<>();
        for(Result result : results) {
            convertedResults.add(resultService.convertResultToDto(result));
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertedResults);
    }

    @GetMapping("{resultId}")
    public ResponseEntity<ResultResponse> getResult(@PathVariable Long resultId) {
        // Get the result from the database using the result service
        Result result = resultService.getResult(resultId);

        // Convert the result into a result response dto and return it
        return ResponseEntity.status(HttpStatus.OK).body(resultService.convertResultToDto(result));
    }

    @PatchMapping("{resultId}")
    public ResponseEntity<ResultResponse> updateResult(@PathVariable Long resultId, @RequestBody ResultRequest resultRequest) {
        // Update the fields of the result with the given id using the result service
        Result result = resultService.updateResult(resultId, resultRequest);

        // Convert the result into a result response dto and return it
        return ResponseEntity.status(HttpStatus.OK).body(resultService.convertResultToDto(result));
    }
}
