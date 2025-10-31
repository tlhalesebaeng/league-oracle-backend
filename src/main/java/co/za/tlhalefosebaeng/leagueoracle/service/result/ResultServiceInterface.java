package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.AddResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;

public interface ResultServiceInterface {
    Result addResult(Long fixtureId, AddResultRequest resultRequest);
}
