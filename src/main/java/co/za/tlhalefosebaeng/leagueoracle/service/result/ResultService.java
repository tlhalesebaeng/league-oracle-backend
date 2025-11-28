package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.ResultRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResultService implements ResultServiceInterface {
    private final ResultRepository resultRepo;
    private final FixtureServiceInterface fixtureService;
    private final LeagueServiceInterface leagueService;

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
        // Get the fixture with the given id from the database using the fixture service. This
        // will also confirm that the fixture exists otherwise it will throw the relevant exception
        Fixture fixture = fixtureService.getFixture(fixtureId);

        // Get the league with fixture league's id from the database using the league service
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

        // Update team details accordingly
        updateTeams(league, result);

        // Save the league using the league service
        leagueService.saveLeague(league);

        // Delete the fixture using the fixture id
        fixtureService.deleteFixture(fixture.getId());

        // Save the result and return it
        return resultRepo.save(result);
    }

    @Override
    public List<Result> getLeagueResults(Long leagueId) {
        // Get the league corresponding to provided league id. This will also confirm that the league exists and throw a relevant exception otherwise
        League league = leagueService.getLeague(leagueId);

        // Retrieve all the results that belong to the above league
        return resultRepo.findAllByLeagueId(league.getId());
    }

    @Override
    public Result getResult(Long resultId) {
        // Get the result from the database using the result repository
        Optional<Result> result = resultRepo.findById(resultId);

        // Return the result or throw a resource not found exception
        return result.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST ,"Result not found! Please check result ID and try again"));
    }

    @Override
    public Result updateResult(Long resultId, ResultRequest requestDto) {
        // Get the result with the given id from the database using the getResult method
        Result result = this.getResult(resultId);

        // Get the league with the result league's id from the database using the league service
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

        // Update the result scores
        result.setHomeTeamScore(newHomeTeamScore);
        result.setAwayTeamScore(newAwayTeamScore);

        // Save the team details
        leagueService.saveLeague(league);

        // Update the result and return the newly saved result
        return resultRepo.save(result);
    }
}
