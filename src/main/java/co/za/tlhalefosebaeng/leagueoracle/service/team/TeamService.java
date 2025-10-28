package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.ResourceNotFoundException;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamServiceInterface{
    private final LeagueServiceInterface leagueService;
    private final LeagueRepository leagueRepo;

    @Override
    public League addTeamToLeague(Long leagueId, AddTeamRequest team) {
        // Get the league from the database
        League league = leagueService.getLeague(leagueId);

        // Set the necessary team properties
        Team newTeam = new Team();
        newTeam.setLeague(league);
        newTeam.setName(team.getName());

        // Add the team to the list of teams and return the newly saved league
        league.getTeams().add(newTeam);
        return leagueRepo.save(league);
    }

    @Override
    public League updateLeagueTeam(Long leagueId, Long teamId, UpdateTeamRequest team) {
        // Get the league from the database using the league service
        League league = leagueService.getLeague(leagueId);

        // Use linear search to store the team that has the provided team id
        // Linear search will perform well since leagues have very few teams
        Team newTeam = null;
        for(Team t : league.getTeams()) {
            if(Objects.equals(t.getId(), teamId)) {
                newTeam = t;
                break;
            }
        }

        // Throw a relevant exception when the league has no team with the given team id
        if(newTeam == null) throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Team not found! Please check team ID and try again.");

        // Update properties of the existing team
        if(team.getName() != null && !team.getName().isBlank()) newTeam.setName(team.getName());
        if(team.getWins() != newTeam.getWins()) newTeam.setWins(team.getWins());
        if(team.getDraws() != newTeam.getDraws()) newTeam.setDraws(team.getDraws());
        if(team.getLoses() != newTeam.getLoses()) newTeam.setLoses(team.getLoses());
        if(team.getGoalsForward() != newTeam.getGoalsForward()) newTeam.setGoalsForward(team.getGoalsForward());
        if(team.getGoalsAgainst() != newTeam.getGoalsAgainst()) newTeam.setGoalsAgainst(team.getGoalsAgainst());

        return leagueRepo.save(league);
    }
}
