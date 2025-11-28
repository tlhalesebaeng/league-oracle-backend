package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamMapper {
    public static TeamResponse toResponse(Team team) {
        TeamResponse response = new TeamResponse();
        response.setId(team.getId());
        response.setName(team.getName());
        response.setWins(team.getWins());
        response.setDraws(team.getDraws());
        response.setLoses(team.getLoses());
        response.setGoalsForward(team.getGoalsForward());
        response.setGoalsAgainst(team.getGoalsAgainst());
        response.setPlayedGames();
        response.setPoints();
        response.setGoalDifference();
        return response;
    }

    public static List<TeamResponse> toResponseList(List<Team> teams) {
        List<TeamResponse> responseList = new ArrayList<>();
        for(Team team : teams) responseList.add(TeamMapper.toResponse(team));
        return responseList;
    }
}
