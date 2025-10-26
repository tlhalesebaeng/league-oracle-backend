package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public League getLeague(Long leagueId) {
        Optional<League> league = leagueRepo.findById(leagueId);
        return league.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "League not found! Please check league ID and try again."));
    }

    @Override
    public List<League> getAllLeagues() {
        return leagueRepo.findAll();
    }

    @Override
    public League updateLeague(Long leagueId, LeagueRequest league) {
        League leagueToUpdate = this.getLeague(leagueId);
        // We should only update the league name. There are endpoints to update other properties of the league
        leagueToUpdate.setName(league.getName());
        return leagueRepo.save(leagueToUpdate);
    }
}
