package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
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
        fixtureResponse.setHomeTeam(teamService.convertTeamToDto(fixture.getHomeTeam()));
        fixtureResponse.setAwayTeam(teamService.convertTeamToDto(fixture.getAwayTeam()));

        return fixtureResponse;
    }

    @Override
    public List<Fixture> generateLeagueFixtures(Long leagueId) {
        // Only home fixtures are generated for now

        // Get the league from the database using the league service
        League league = leagueService.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to generate fixtures
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Confirm that the league has no fixtures already
        if(!league.getFixtures().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot create league fixtures! The league already has fixtures");
        }

        // Verify that the league has no results - If it does then all fixtures have been played
        if(!league.getResults().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot create league fixtures! All fixtures have been played");
        }

        List<Team> teams = league.getTeams(); // Get the league teams. For easy reference

        // Generate fixtures. The collection we used for storing teams does not store them in the order they arrive, this increases randomization of the fixture generator
        List<Fixture> fixtures = new ArrayList<>();
        for(int i = 0; i < teams.size() - 1; i++) {
            boolean isHome = true; // Keep whether we should make the team at index i home or away

            for(int j = i+1; j < teams.size(); j++) {
                Team t1 = teams.get(i);
                Team t2 = teams.get(j);
                // With this implementation the teams will never be similar

                Fixture fixture = new Fixture();
                fixture.setLeague(league);
                if(isHome) {
                    fixture.setHomeTeam(t1);
                    fixture.setAwayTeam(t2);
                } else {
                    fixture.setHomeTeam(t2);
                    fixture.setAwayTeam(t1);
                }

                // Add the saved fixture to the fixture list
                fixtures.add(fixtureRepo.save(fixture));

                isHome = !isHome;
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
        return fixture.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Fixture not found! Please check fixture ID and try again"));
    }

    @Override
    public Fixture updateFixture(Long fixtureId, UpdateFixtureRequest fixtureRequest) {
        // Get the fixture with the provided id, this will also confirm that the fixture exists
        Fixture fixture = this.getFixture(fixtureId);

        // Get the league with fixture league's id from the database using the league service
        League league = leagueService.getLeague(fixture.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of the league should be able to update league fixtures
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Set the fields if they are provided. The dto makes sure that these fields are not empty
        if(fixtureRequest.getDate() != null) fixture.setDate(fixtureRequest.getDate());
        if(fixtureRequest.getVenue() != null) fixture.setVenue(fixtureRequest.getVenue());
        if(fixtureRequest.getField() != null) fixture.setField(fixtureRequest.getField());

        return fixtureRepo.save(fixture);
    }

    @Override
    public void deleteFixture(Long fixtureId) {
        // Get the fixture from the database. This will also confirm that the fixture exists
        Fixture fixture = this.getFixture(fixtureId);

        // Get the league with the fixture league's id from the database using the league service
        League league = leagueService.getLeague(fixture.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete a fixture
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        fixtureRepo.delete(fixture); // Delete the fixture using the fixture repo
    }

}
