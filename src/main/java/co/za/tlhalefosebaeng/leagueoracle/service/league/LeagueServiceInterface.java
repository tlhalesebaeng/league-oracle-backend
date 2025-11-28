package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;

import java.util.List;

public interface LeagueServiceInterface {
    League createLeague(LeagueRequest league);
    League getLeague(Long leagueId);
    List<League> getAllLeagues(String name);
    League updateLeague(Long leagueId, LeagueRequest requestDto);
    void deleteLeague(Long leagueId);
    boolean isCreator(League league);
    List<League> getMyLeagues();
}
