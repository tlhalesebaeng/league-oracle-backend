package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.FixtureRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FixtureService implements FixtureServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixtureService.class);
    private final FixtureRepository fixtureRepo;
    private final LeagueServiceInterface leagueService;
    private final HttpServletRequest request;

    @Override
    public List<Fixture> generateLeagueFixtures(Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing Fixture Generation: {} LeagueId {}", correlationId, leagueId);

        // Only home fixtures are generated for now

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
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch all league fixtures: {} LeagueId {}", correlationId, leagueId);
        League league = leagueService.getLeague(leagueId);
        List<Fixture> fixtures = fixtureRepo.findAllByLeagueId(league.getId());
        LOGGER.info("Successfully fetched all league fixtures: {} Fixtures {}", correlationId, fixtures.size());
        return fixtures;
    }

    @Override
    public Fixture getFixture(Long fixtureId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch fixture: {} FixtureId {}", correlationId, fixtureId);
        Optional<Fixture> dbFixture = fixtureRepo.findById(fixtureId);
        Fixture fixture = dbFixture.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Fixture not found! Please check fixture ID and try again"));
        LOGGER.info("Successfully fetched fixture: {} FixtureId {}", correlationId, fixture.getId()); // Log the fixture id to confirm that the ids are the same
        return fixture;
    }

    @Override
    public List<Fixture> getUpcomingFixtures(Integer month) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch upcoming fixtures: {} Month {}", correlationId, month);
        List<League> leagues = leagueService.getMyLeagues();

        // Generate a list of all the fixtures that are scheduled for the given month
        List<Fixture> fixtures = new ArrayList<>();
        for(League league : leagues) {
            for(Fixture fixture : league.getFixtures()) {
                LocalDate date = fixture.getDate();
                if(date != null && date.isAfter(LocalDate.now()) && date.getMonthValue() == month) {
                    fixtures.add(fixture);
                }
            }
        }

        LOGGER.info("Successfully fetched upcoming fixtures: {} Fixtures {}", correlationId, fixtures.size());
        return fixtures;
    }

    @Override
    public Fixture updateFixture(Long fixtureId, UpdateFixtureRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to update fixture: {} FixtureId {}", correlationId, fixtureId);
        Fixture fixture = this.getFixture(fixtureId);
        League league = leagueService.getLeague(fixture.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of the league should be able to update league fixtures
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Set the fields if they are provided. The dto makes sure that these fields are not empty
        if(requestDto.getDate() != null) fixture.setDate(requestDto.getDate());
        if(requestDto.getTime() != null) fixture.setTime(requestDto.getTime());
        if(requestDto.getVenue() != null) fixture.setVenue(requestDto.getVenue());
        if(requestDto.getField() != null) fixture.setField(requestDto.getField());

        Fixture updatedFixture = fixtureRepo.save(fixture);
        LOGGER.info("Successfully updated fixture: {} FixtureId {}", correlationId, updatedFixture.getId()); // Log the updated fixture id to confirm that the ids are the same
        return updatedFixture;
    }

    @Override
    public void deleteFixture(Long fixtureId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to delete fixture: {} FixtureId {}", correlationId, fixtureId);
        Fixture fixture = this.getFixture(fixtureId);
        League league = leagueService.getLeague(fixture.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete a fixture
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        fixtureRepo.delete(fixture);
        LOGGER.info("Successfully deleted fixture: {}", correlationId);
    }

}
