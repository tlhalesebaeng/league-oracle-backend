package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.AddResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;
import org.springframework.stereotype.Service;

@Service
public class ResultService implements ResultServiceInterface {
    @Override
    public Result addResult(Long fixtureId, AddResultRequest resultRequest) {
        return null;
    }
}
