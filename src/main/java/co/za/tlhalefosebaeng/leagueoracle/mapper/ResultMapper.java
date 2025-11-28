package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultMapper {
    public static ResultResponse toResponse(Result result) {
        ResultResponse response = new ResultResponse();

        response.setId(result.getId());
        response.setName(result.getLeague().getName());
        response.setDate(result.getDate());
        response.setTime(result.getTime());
        response.setHomeTeamScore(result.getHomeTeamScore());
        response.setAwayTeamScore(result.getAwayTeamScore());
        response.setHomeTeam(TeamMapper.toResponse(result.getHomeTeam()));
        response.setAwayTeam(TeamMapper.toResponse(result.getAwayTeam()));

        return response;
    }

    public static List<ResultResponse> toResponseList(List<Result> results) {
        List<ResultResponse> responseList = new ArrayList<>();
        for(Result result : results) responseList.add(ResultMapper.toResponse(result));
        return responseList;
    }
}
