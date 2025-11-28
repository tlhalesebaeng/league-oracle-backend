package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;

public interface TeamServiceInterface {
    Team addTeam(Long leagueId, AddTeamRequest requestDto);
    Team getTeam(Long teamId);
    Team updateTeam(Long teamId, UpdateTeamRequest requestDto);
    void deleteTeam(Long teamId);
}
