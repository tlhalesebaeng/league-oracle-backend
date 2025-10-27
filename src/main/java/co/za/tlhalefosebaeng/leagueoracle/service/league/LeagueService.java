package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
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

    // Helper method to convert a league team to a team response DTO
    private TeamResponse convertTeamToDto(Team team) {
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setId(team.getId());
        teamResponse.setName(team.getName());
        teamResponse.setWins(team.getWins());
        teamResponse.setDraws(team.getDraws());
        teamResponse.setLoses(team.getLoses());
        teamResponse.setGoalsForward(team.getGoalsForward());
        teamResponse.setGoalsAgainst(team.getGoalsAgainst());
        teamResponse.setPlayedGames();
        teamResponse.setPoints();
        teamResponse.setGoalDifference();
        return teamResponse;
    }

    // Helper method to convert a league to the league response DTO
    private LeagueResponse convertLeagueToDto(League league) {
        LeagueResponse leagueResponse = new LeagueResponse();
        leagueResponse.setName(league.getName());

        // Convert the league teams to team response DTO
        List<TeamResponse> teamResponses = league.getTeams().stream().map(this::convertTeamToDto).toList();
        leagueResponse.setTeams(teamResponses);

        return leagueResponse;
    }

    @Override
    public LeagueResponse createLeague(LeagueRequest league) {
        League pendingLeague = new League();
        pendingLeague.setName(league.getName());

        // Assign the reference of the pending league to all teams
        for(Team team : league.getTeams()) {
            team.setLeague(pendingLeague);
        }

        // Assign the refence of teams to the league
        pendingLeague.setTeams(league.getTeams());

        // Save the league and return the saved league

        return convertLeagueToDto(leagueRepo.save(pendingLeague));
    }

    @Override
    public League getLeague(Long leagueId) {
        Optional<League> league = leagueRepo.findById(leagueId);
        return league.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "League not found! Please check league ID and try again."));
    }

    @Override
    public List<LeagueResponse> getAllLeagues(String name) {
        // Get all the leagues from the database
        List<League> leagues = null;
        if(name != null) leagues = leagueRepo.findAllByLeagueName(name);
        else leagues = leagueRepo.findAll();

        // Convert the leagues to the league response dto and return them
        return leagues.stream().map(this::convertLeagueToDto).toList();
    }

    @Override
    public League updateLeague(Long leagueId, LeagueRequest league) {
        League leagueToUpdate = this.getLeague(leagueId);
        // We should only update the league name. There are endpoints to update other properties of the league
        leagueToUpdate.setName(league.getName());
        return leagueRepo.save(leagueToUpdate);
    }

    @Override
    public void deleteLeague(Long leagueId) {
        League leagueToDelete = this.getLeague(leagueId);
        leagueRepo.delete(leagueToDelete);
    }
}
