package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.repository.FixtureRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FixtureService implements FixtureServiceInterface {
    private final FixtureRepository fixtureRepo;
    private final LeagueServiceInterface leagueService;
    private final TeamServiceInterface teamService;

    @Override
    public FixtureResponse convertFixtureToDto(Fixture fixture) {
        FixtureResponse fixtureResponse = new FixtureResponse();

        fixtureResponse.setId(fixture.getId());
        fixtureResponse.setDate(fixture.getDate());
        fixtureResponse.setTime(fixture.getDate());
        fixtureResponse.setVenue(fixture.getVenue());
        fixtureResponse.setField(fixture.getField());
        fixtureResponse.setName(fixture.getLeague().getName());
        fixtureResponse.setHomeTeam(teamService.convertTeamToDto(fixture.getHomeTeam()));
        fixtureResponse.setAwayTeam(teamService.convertTeamToDto(fixture.getAwayTeam()));

        return fixtureResponse;
    }

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

    @Override
    public List<Fixture> getAllLeagueFixtures(Long leagueId) {
        // Confirm that the league with the given id exists using the league service
        League league = leagueService.getLeague(leagueId);

        // Find all the fixtures that belong to the retrieved league using the league repository
        return fixtureRepo.findAllByLeagueId(league.getId());
    }

    @Override
    public Fixture getFixture(Long fixtureId) {
        // Get the fixture from the database using the repo
        Optional<Fixture> fixture = fixtureRepo.findById(fixtureId);

        // Return the fixture if it exists otherwise throw the relevant exception
        return fixture.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Fixture not found! Please check fixture ID and try again"));
    }

    @Override
    public Fixture updateFixture(Long fixtureId, UpdateFixtureRequest fixtureRequest) {
        // Get the fixture with the provided id, this will also confirm that the fixture exists
        Fixture fixture = this.getFixture(fixtureId);

        // Set the fields if they are provided. The dto makes sure that these fields are not empty
        if(fixtureRequest.getDate() != null) fixture.setDate(fixtureRequest.getDate());
        if(fixtureRequest.getTime() != null) {
            // Update the time of the fixture date
        }
        if(fixtureRequest.getVenue() != null) fixture.setVenue(fixtureRequest.getVenue());
        if(fixtureRequest.getField() != null) fixture.setField(fixtureRequest.getField());

        return fixtureRepo.save(fixture);
    }

    @Override
    public void deleteFixture(Long fixtureId) {
        Fixture fixture = this.getFixture(fixtureId);
        fixtureRepo.delete(fixture);
    }

}
