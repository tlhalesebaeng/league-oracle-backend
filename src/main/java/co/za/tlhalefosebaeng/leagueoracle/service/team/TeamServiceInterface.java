package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;

public interface TeamServiceInterface {
    League addTeam(Long leagueId, AddTeamRequest requestDto);
    League updateTeam(Long leagueId, Long teamId, UpdateTeamRequest requestDto);
    void deleteTeam(Long teamId);
}
