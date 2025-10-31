package co.za.tlhalefosebaeng.leagueoracle.service.result;

import co.za.tlhalefosebaeng.leagueoracle.dto.result.AddResultRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.result.ResultResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Result;
import co.za.tlhalefosebaeng.leagueoracle.repository.ResultRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.fixture.FixtureServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.team.TeamServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Result addResult(Long fixtureId, AddResultRequest resultRequest) {
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
}
