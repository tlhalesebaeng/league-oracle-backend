package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.DuplicateTeamNamesException;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.NotCreatorException;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.user.AppUserDetailsService;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LeagueService implements LeagueServiceInterface {
    private final LeagueRepository leagueRepo;
    private final UserServiceInterface userService;
    private final AppUserDetailsService userDetailsService;

    // Helper method to convert a league to the league response DTO
    @Override
    public LeagueResponse convertLeagueToDto(League league, Function<Team, TeamResponse> convertTeamToDto) {
        LeagueResponse leagueResponse = new LeagueResponse();
        leagueResponse.setId(league.getId());
        leagueResponse.setName(league.getName());
        leagueResponse.setCreator(league.getCreator().getId());

        // Convert the league teams to team response DTO
        List<TeamResponse> teamResponses = new ArrayList<>();
        for(Team team : league.getTeams()) {
            teamResponses.add(convertTeamToDto.apply(team));
        }

        // Set the league response's teams to the convert team response dtos
        leagueResponse.setTeams(teamResponses);

        return leagueResponse;
    }

    // Method to verify that the logged-in user is the creator of the given league.
    // This method should be used under protected routes only as the logged-in user details are required
    @Override
    public boolean isCreator(League league) {
        String email = league.getCreator().getEmail(); // Get the email of the leagues creator
        String username = userDetailsService.getPrincipal().getUsername(); // Get the username of the principal user
        return username.equals(email); // Emails are unique so if they match then the principal user is the creator of the league
    }

    // Helper method to assist with saving the league. This will allow us to save leagues
    // on other services without directly using the league repo
    @Override
    public League saveLeague(League league) {
        return leagueRepo.save(league);
    }

    @Override
    public League createLeague(LeagueRequest league) {
        // Confirm that the team names are unique
        if(!league.validateTeam()) {
            throw new DuplicateTeamNamesException("Invalid details! Teams should have different names");
        }

        // Instantiate a new league instance that will be saved on the database and set its name
        League pendingLeague = new League();
        pendingLeague.setName(league.getName());

        // Get the details of the logged-in user from the user details service and set the creator of the league
        User user = userService.getUserByEmail(userDetailsService.getPrincipal().getUsername());
        pendingLeague.setCreator(user);

        // Assign the reference of the pending league to all teams
        for(Team team : league.getTeams()) {
            team.setLeague(pendingLeague);
        }

        // Assign the refence of teams to the league
        pendingLeague.setTeams(league.getTeams());

        // Save the league and return the saved league
        return this.saveLeague(pendingLeague);
    }

    @Override
    public League getLeague(Long leagueId) {
        Optional<League> league = leagueRepo.findById(leagueId);
        return league.orElseThrow(() -> new ResourceNotFoundException("League not found! Please check league ID and try again."));
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
        return this.saveLeague(leagueToUpdate);
    }

    @Override
    public void deleteLeague(Long leagueId) {
        League leagueToDelete = this.getLeague(leagueId);
        leagueRepo.delete(leagueToDelete);
    }

}
