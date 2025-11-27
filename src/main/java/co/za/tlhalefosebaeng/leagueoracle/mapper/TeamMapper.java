package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;

public class TeamMapper {
    // Method to convert team objects to team response dtos
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
}
