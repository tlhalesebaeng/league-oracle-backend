package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;

import java.util.ArrayList;
import java.util.List;

public class FixtureMapper {
    public static FixtureResponse toResponse(Fixture fixture) {
        FixtureResponse response = new FixtureResponse();

        response.setId(fixture.getId());
        response.setLeague(fixture.getLeague().getId());
        response.setDate(fixture.getDate());
        response.setTime(fixture.getTime());
        response.setVenue(fixture.getVenue());
        response.setField(fixture.getField());
        response.setHomeTeam(TeamMapper.toResponse(fixture.getHomeTeam()));
        response.setAwayTeam(TeamMapper.toResponse(fixture.getAwayTeam()));

        return response;
    }

    public static List<FixtureResponse> toResponseList(List<Fixture> fixtures) {
        List<FixtureResponse> responseList = new ArrayList<>();
        for(Fixture fixture : fixtures) responseList.add(FixtureMapper.toResponse(fixture));
        return responseList;
    }
}
