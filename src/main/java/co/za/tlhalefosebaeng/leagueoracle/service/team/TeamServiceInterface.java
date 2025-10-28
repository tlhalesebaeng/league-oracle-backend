package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;

public interface TeamServiceInterface {
    League addTeamToLeague(Long leagueId, AddTeamRequest team);
    League updateLeagueTeam(Long leagueId, Long teamId, UpdateTeamRequest team);
}
