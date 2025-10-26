package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;

public interface LeagueServiceInterface {
    League createLeague(LeagueRequest league);
    League getLeague(Long leagueId);
}
