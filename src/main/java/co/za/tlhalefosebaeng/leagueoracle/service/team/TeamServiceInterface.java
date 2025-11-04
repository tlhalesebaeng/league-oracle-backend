package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;

public interface TeamServiceInterface {
    League addTeam(Long leagueId, AddTeamRequest team);
    League updateTeam(Long leagueId, Long teamId, UpdateTeamRequest team);
    void deleteTeam(Long teamId);
    TeamResponse convertTeamToDto(Team team);
}
