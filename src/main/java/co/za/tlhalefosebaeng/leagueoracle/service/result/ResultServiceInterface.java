package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;

import java.util.List;

public interface ResultServiceInterface {
    ResultResponse convertResultToDto(Result result);
    Result addResult(Long fixtureId, ResultRequest resultRequest);
    List<Result> getLeagueResults(Long leagueId);
    Result getResult(Long resultId);
    Result updateResult(Long resultId, ResultRequest resultRequest);
}
