package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.AddResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;

import java.util.List;

public interface ResultServiceInterface {
    ResultResponse convertResultToDto(Result result);
    Result addResult(Long fixtureId, AddResultRequest resultRequest);
    List<Result> getLeagueResults(Long leagueId);
}
