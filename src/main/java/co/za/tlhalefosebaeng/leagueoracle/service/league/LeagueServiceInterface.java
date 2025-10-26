package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;

import java.util.List;

public interface LeagueServiceInterface {
    League createLeague(LeagueRequest league);
    League getLeague(Long leagueId);
    List<League> getAllLeagues();
    League updateLeague(Long leagueId, LeagueRequest league);
}
