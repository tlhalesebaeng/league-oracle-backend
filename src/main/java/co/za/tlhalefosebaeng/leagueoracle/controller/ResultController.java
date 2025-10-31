package co.za.tlhalefosebaeng.leagueoracle.controller;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.AddResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;
import co.za.tlhalefosebaeng.leagueoracle.response.ApiResponse;
import co.za.tlhalefosebaeng.leagueoracle.service.result.ResultServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.prefix}/results")
public class ResultController {
    private final ResultServiceInterface resultService;

    @PostMapping("")
    public ResponseEntity<ApiResponse> addResult(@RequestParam Long fixtureId, @RequestBody AddResultRequest resultRequest) {
        // Add results of a fixture to the database using the result service
        Result result = resultService.addResult(fixtureId, resultRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", resultService.convertResultToDto(result)));
    }

}
