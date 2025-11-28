package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.Result;

import java.util.List;

public interface ResultServiceInterface {
    Result addResult(Long fixtureId, ResultRequest requestDto);
    List<Result> getLeagueResults(Long leagueId);
    Result getResult(Long resultId);
    Result updateResult(Long resultId, ResultRequest requestDto);
}
