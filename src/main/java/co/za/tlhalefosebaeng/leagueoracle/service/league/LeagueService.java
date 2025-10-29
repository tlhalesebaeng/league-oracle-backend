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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LeagueService implements LeagueServiceInterface {
    private final LeagueRepository leagueRepo;

    // Helper method to convert a league to the league response DTO
    @Override
    public LeagueResponse convertLeagueToDto(League league, Function<Team, TeamResponse> convertTeamToDto) {
        LeagueResponse leagueResponse = new LeagueResponse();
        leagueResponse.setId(league.getId());
        leagueResponse.setName(league.getName());

        // Convert the league teams to team response DTO
        List<TeamResponse> teamResponses = new ArrayList<>();
        for(Team team : league.getTeams()) {
            teamResponses.add(convertTeamToDto.apply(team));
        }

        // Set the league response's teams to the convert team response dtos
        leagueResponse.setTeams(teamResponses);

        return leagueResponse;
    }

    // Helper method to assist with saving the league. This will allow us to save leagues
    // on other services without directly using the league repo
    @Override
    public League saveLeague(League league) {
        return leagueRepo.save(league);
    }

    @Override
    public League createLeague(LeagueRequest league) {
        League pendingLeague = new League();
        pendingLeague.setName(league.getName());

        // Assign the reference of the pending league to all teams
        for(Team team : league.getTeams()) {
            team.setLeague(pendingLeague);
        }

        // Assign the refence of teams to the league
        pendingLeague.setTeams(league.getTeams());

        // Save the league and return the saved league
        return leagueRepo.save(pendingLeague);
    }

    @Override
    public League getLeague(Long leagueId) {
        Optional<League> league = leagueRepo.findById(leagueId);
        return league.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "League not found! Please check league ID and try again."));
    }

    @Override
    public List<League> getAllLeagues(String name) {
        if(name != null) return leagueRepo.findAllByLeagueName(name);
        return leagueRepo.findAll();
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
