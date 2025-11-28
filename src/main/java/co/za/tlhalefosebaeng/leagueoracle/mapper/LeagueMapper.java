package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeagueMapper {
    public static LeagueResponse toResponse(League league) {
        LeagueResponse response = new LeagueResponse();
        response.setId(league.getId());
        response.setName(league.getName());
        response.setCreator(league.getCreator().getId());
        response.setTeams(TeamMapper.toResponseList(league.getTeams()));

        // Set the date in the format yyyy-mm-dd
        LocalDate date = league.getCreatedAt().toLocalDate();
        response.setCreatedAt(date.toString());

        return response;
    }

    public static List<LeagueResponse> toResponseList(List<League> leagues) {
        List<LeagueResponse> responseList = new ArrayList<>();
        for(League league : leagues) responseList.add(LeagueMapper.toResponse(league));
        return responseList;
    }
}
