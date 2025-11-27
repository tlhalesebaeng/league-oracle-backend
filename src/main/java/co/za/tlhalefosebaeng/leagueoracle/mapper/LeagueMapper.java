package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.League;

import java.time.LocalDate;

public class LeagueMapper {
    // Method used to convert league objects to league response dtos
    public static LeagueResponse toResponse(League league) {
        LeagueResponse response = new LeagueResponse();
        response.setId(league.getId());
        response.setName(league.getName());
        response.setCreator(league.getCreator().getId());
        response.setTeams(TeamMapper.toResponseList(league.getTeams()));

        // Convert the date the league was created at to a local date object
        LocalDate date = league.getCreatedAt().toLocalDate();
        response.setCreatedAt(date.toString()); // Get the yyyy-mm-dd format of the local date

        return response;
    }
}
