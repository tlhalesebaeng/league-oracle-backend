package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeagueService implements LeagueServiceInterface {
    private final LeagueRepository leagueRepo;

    @Override
    public League createLeague(LeagueRequest league) {
        League pendingLeague = new League();
        pendingLeague.setName(league.getName());
        return leagueRepo.save(pendingLeague);
    }
}
