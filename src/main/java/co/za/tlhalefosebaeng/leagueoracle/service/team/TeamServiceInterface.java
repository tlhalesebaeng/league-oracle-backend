package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;

public interface TeamServiceInterface {
    League addTeamToLeague(Long leagueId, AddTeamRequest team);
}
