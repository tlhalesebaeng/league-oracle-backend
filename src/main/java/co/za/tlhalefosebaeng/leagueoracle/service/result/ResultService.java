package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;
import co.za.tlhalefosebaeng.leagueoracle.repository.ResultRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
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
    private final TeamServiceInterface teamService;
    private final LeagueServiceInterface leagueService;

    @Override
    public ResultResponse convertResultToDto(Result result) {
        ResultResponse resultResponse = new ResultResponse();

        resultResponse.setId(result.getId());
        resultResponse.setName(result.getLeague().getName());
        resultResponse.setDate(result.getDate());
        resultResponse.setTime(result.getDate());
        resultResponse.setHomeTeamScore(result.getHomeTeamScore());
        resultResponse.setAwayTeamScore(result.getAwayTeamScore());
        resultResponse.setHomeTeam(teamService.convertTeamToDto(result.getHomeTeam()));
        resultResponse.setAwayTeam(teamService.convertTeamToDto(result.getAwayTeam()));

        return resultResponse;
    }

    @Override
    public Result addResult(Long fixtureId, ResultRequest resultRequest) {
        // Get the fixture with the given id from the database using the fixture service. This
        // will also confirm that the fixture exists otherwise it will throw the relevant exception
        Fixture fixture = fixtureService.getFixture(fixtureId);

        // Create a new instance of result and set the properties accordingly
        Result result = new Result();
        result.setLeague(fixture.getLeague());
        result.setDate(fixture.getDate());
        result.setHomeTeam(fixture.getHomeTeam());
        result.setAwayTeam(fixture.getAwayTeam());
        result.setHomeTeamScore(resultRequest.getHomeTeamScore());
        result.setAwayTeamScore(resultRequest.getAwayTeamScore());

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
        return result.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Result not found! Please check result ID and try again"));
    }

    @Override
    public Result updateResult(Long resultId, ResultRequest resultRequest) {
        // Get the result with the given id from the database using the getResult method
        Result result = this.getResult(resultId);

        // Update the result fields when there are differences. We only set the team scores to keep integrity and reliability
        // of the system since only the scores of the fixture are what should really be changed
        if(!Objects.equals(result.getHomeTeamScore(), resultRequest.getHomeTeamScore())) {
            result.setHomeTeamScore(resultRequest.getHomeTeamScore());
        }

        if(!Objects.equals(result.getAwayTeamScore(), resultRequest.getAwayTeamScore())) {
            result.setAwayTeamScore(resultRequest.getAwayTeamScore());
        }

        // Update the result and return the newly saved result
        return resultRepo.save(result);
    }
}
