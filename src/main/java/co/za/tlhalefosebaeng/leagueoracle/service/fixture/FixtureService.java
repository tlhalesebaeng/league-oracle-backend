package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.FixtureRepository;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FixtureService implements FixtureServiceInterface {
    private final FixtureRepository fixtureRepo;
    private final LeagueServiceInterface leagueService;

    @Override
    public List<Fixture> generateLeagueFixtures(Long leagueId) {
        // Only home fixtures are generated for now

        // Get the league from the database using the league service
        League league = leagueService.getLeague(leagueId);

        // Generate fixtures
        List<Fixture> fixtures = new ArrayList<>();
        int size = league.getTeams().size();
        for(int i = 0; i < size; i++) {
            for(int j = size - 1; j > 0; j--) {
                if(i != j) { // A team cannot play itself
                    Fixture fixture = new Fixture();
                    fixture.setLeague(league);
                    fixture.setHomeTeam(league.getTeams().get(i));
                    fixture.setAwayTeam(league.getTeams().get(j));

                    // Add the saved fixture to the fixture list
                    fixtures.add(fixtureRepo.save(fixture));
                }
            }
        }

        return fixtures;
    }
}
