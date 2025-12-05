package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Result;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.ResultRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResultService implements ResultServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultService.class);
    private final ResultRepository resultRepo;
    private final FixtureServiceInterface fixtureService;
    private final LeagueServiceInterface leagueService;
    private final HttpServletRequest request;

    // Helper method that updates team details out of the provided results
    private void updateTeams(League league, Result result) {
        // Get the home and away team from the league
        Team homeTeam = null;
        Team awayTeam = null;
        for(Team team : league.getTeams()) {
            if(Objects.equals(team.getId(), result.getHomeTeam().getId())) {
                // Set the home team
                homeTeam = team;
            }

            if(Objects.equals(team.getId(), result.getAwayTeam().getId())) {
                // Set the away team
                awayTeam = team;
            }
        }

        // Avoid null pointer exception
        if(homeTeam != null && awayTeam != null) {
            homeTeam.setGoalsAgainst(homeTeam.getGoalsAgainst() + result.getAwayTeamScore());
            awayTeam.setGoalsAgainst(awayTeam.getGoalsAgainst() + result.getHomeTeamScore());

            homeTeam.setGoalsForward(homeTeam.getGoalsForward() + result.getHomeTeamScore());
            awayTeam.setGoalsForward(awayTeam.getGoalsForward() + result.getAwayTeamScore());

            // Determine the match results and continue to update fields
            if(result.getHomeTeamScore() > result.getAwayTeamScore()) {
                // Home team won the match
                homeTeam.setWins(homeTeam.getWins() + 1); // Increment home team wins
                awayTeam.setLoses(awayTeam.getLoses() + 1); // Increment away tem losses
            } else if (result.getHomeTeamScore() < result.getAwayTeamScore()) {
                // Away team won the match
                homeTeam.setLoses(homeTeam.getLoses() + 1); // Increment home team losses
                awayTeam.setWins(awayTeam.getWins() + 1); // Increment away tem wins
            } else {
                // The match is a draw
                homeTeam.setDraws(homeTeam.getDraws() + 1);
                awayTeam.setDraws(awayTeam.getDraws() + 1);
            }
        }
    }

    @Override
    public Result addResult(Long fixtureId, ResultRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to add fixture result: {} LeagueId {}", correlationId, fixtureId);

        Fixture fixture = fixtureService.getFixture(fixtureId);
        League league = leagueService.getLeague(fixture.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of the league should be able to add league results
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Create a new instance of result and set the properties accordingly
        Result result = new Result();
        result.setLeague(fixture.getLeague());
        result.setDate(fixture.getDate());
        result.setTime(fixture.getTime());
        result.setHomeTeam(fixture.getHomeTeam());
        result.setAwayTeam(fixture.getAwayTeam());
        result.setHomeTeamScore(requestDto.getHomeTeamScore());
        result.setAwayTeamScore(requestDto.getAwayTeamScore());

        updateTeams(league, result);

        fixtureService.deleteFixture(fixture.getId());
        Result dbResult = resultRepo.save(result);
        LOGGER.info("Successfully added fixture result: {} ResultId {}", correlationId, dbResult.getId());
        return dbResult;
    }

    @Override
    public List<Result> getLeagueResults(Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch all league results: {} LeagueId {}", correlationId, leagueId);
        League league = leagueService.getLeague(leagueId);
        List<Result> results = resultRepo.findAllByLeagueId(league.getId());
        LOGGER.info("Successfully fetched all league results: {} LeagueId {} Results {}", correlationId, leagueId, results.size());
        return results;
    }

    @Override
    public Result getResult(Long resultId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch result: {} ResultId {}", correlationId, resultId);
        Optional<Result> dbResult = resultRepo.findById(resultId);
        Result result = dbResult.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST ,"Result not found! Please check result ID and try again"));
        LOGGER.info("Successfully fetched result: {} ResultId {}", correlationId, result.getId()); // Log the result id to confirm that the ids are the same
        return result;
    }

    @Override
    public Result updateResult(Long resultId, ResultRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to update result: {} ResultId {}", correlationId, resultId);
        Result result = this.getResult(resultId);
        League league = leagueService.getLeague(result.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of the league should be able to update league results
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Get the home and away team from the league
        Team homeTeam = null;
        Team awayTeam = null;
        for(Team team : league.getTeams()) {
            if(Objects.equals(team.getId(), result.getHomeTeam().getId())) {
                // Set the home team
                homeTeam = team;
            }

            if(Objects.equals(team.getId(), result.getAwayTeam().getId())) {
                // Set the away team
                awayTeam = team;
            }
        }

        // Update the result fields when there are differences. We only set the team scores to keep integrity and reliability
        // of the system since only the scores of the fixture are what should really be changed

        int newHomeTeamScore = requestDto.getHomeTeamScore();
        int newAwayTeamScore = requestDto.getAwayTeamScore();

        int oldHomeTeamScore = result.getHomeTeamScore();
        int oldAwayTeamScore = result.getAwayTeamScore();

        // Determine the new outcomes for each team
        String homeTeamOutcome;
        if (newHomeTeamScore > newAwayTeamScore) {
            homeTeamOutcome = "win";
        } else if (newHomeTeamScore < newAwayTeamScore) {
            homeTeamOutcome = "lose";
        } else {
            homeTeamOutcome = "draw";
        }

        // Set the team details according to the home team outcome
        if (oldHomeTeamScore < oldAwayTeamScore) {
            if(homeTeamOutcome.equals("win")) {
                // Home team had lost now it won the match
                homeTeam.setWins(homeTeam.getWins() + 1);
                homeTeam.setLoses(homeTeam.getLoses() - 1);

                // Away team had won now it lost the match
                awayTeam.setWins(awayTeam.getWins() - 1);
                awayTeam.setLoses(awayTeam.getLoses() + 1);
            } else if(homeTeamOutcome.equals("draw")) {
                // Home team had lost now the match is drawn
                homeTeam.setLoses(homeTeam.getLoses() - 1);
                homeTeam.setDraws(homeTeam.getDraws() + 1);

                // Away team had won now the match is drawn
                awayTeam.setWins(awayTeam.getWins() - 1);
                awayTeam.setDraws(awayTeam.getDraws() + 1);
            }

        } else if (oldHomeTeamScore > oldAwayTeamScore) {
            if(homeTeamOutcome.equals("lose")) {
                // Home team had won now it lost the match
                homeTeam.setWins(homeTeam.getWins() - 1);
                homeTeam.setLoses(homeTeam.getLoses() + 1);

                // Away team had lost now it won the match
                awayTeam.setWins(awayTeam.getWins() + 1);
                awayTeam.setLoses(awayTeam.getLoses() - 1);
            } else if(homeTeamOutcome.equals("draw")) {
                // Home team had won now the match is drawn
                homeTeam.setWins(homeTeam.getWins() - 1);
                homeTeam.setDraws(homeTeam.getLoses() + 1);

                // Away team had lost now the match is drawn
                awayTeam.setLoses(awayTeam.getLoses() - 1);
                awayTeam.setDraws(awayTeam.getLoses() + 1);
            }

        } else if (oldHomeTeamScore == oldAwayTeamScore) {
            if(homeTeamOutcome.equals("win")) {
                // The match was drawn now the home team has won the match
                homeTeam.setDraws(homeTeam.getDraws() - 1);
                homeTeam.setWins(homeTeam.getWins() + 1);

                // The match was drawn now the away team has lost the match
                awayTeam.setDraws(awayTeam.getDraws() - 1);
                awayTeam.setLoses(awayTeam.getLoses() + 1);
            } else if(homeTeamOutcome.equals("lose")){
                // The match was drawn now the home team has lost the match
                homeTeam.setDraws(homeTeam.getDraws() - 1);
                homeTeam.setLoses(homeTeam.getLoses() + 1);

                // The match was drawn now the away team has won the match
                awayTeam.setDraws(awayTeam.getDraws() - 1);
                awayTeam.setWins(awayTeam.getWins() + 1);
            }
        }

        // Determine the score differences and update the goal against and forward of teams
        int homeTeamScoreDifference = oldHomeTeamScore - newHomeTeamScore;
        int awayTeamScoreDifference = oldAwayTeamScore - newAwayTeamScore;

        homeTeam.setGoalsForward(homeTeam.getGoalsForward() - homeTeamScoreDifference);
        homeTeam.setGoalsAgainst(homeTeam.getGoalsAgainst() - awayTeamScoreDifference);

        awayTeam.setGoalsForward(awayTeam.getGoalsForward() - awayTeamScoreDifference);
        awayTeam.setGoalsAgainst(awayTeam.getGoalsAgainst() - homeTeamScoreDifference);

        result.setHomeTeamScore(newHomeTeamScore);
        result.setAwayTeamScore(newAwayTeamScore);

        Result dbResult = resultRepo.save(result);
        LOGGER.info("Successfully updated result: {} ResultId {}", correlationId, dbResult); // Log the result id to confirm that the ids are the same
        return dbResult;
    }
}
