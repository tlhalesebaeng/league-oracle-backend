package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.TeamResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.TeamRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamServiceInterface{
    private final LeagueServiceInterface leagueService;
    private final TeamRepository teamRepo;

    // Helper method to convert a league team to a team response DTO
    @Override
    public TeamResponse convertTeamToDto(Team team) {
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

    @Override
    public League addTeam(Long leagueId, AddTeamRequest team) {
        // Get the league from the database using the league service
        League league = leagueService.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to add teams of a league
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Set the necessary team properties
        Team newTeam = new Team();
        newTeam.setLeague(league);
        newTeam.setName(team.getName());

        // Add the team to the list of teams and return the newly saved league
        league.getTeams().add(newTeam);
        return leagueService.saveLeague(league);
    }

    @Override
    public League updateTeam(Long leagueId, Long teamId, UpdateTeamRequest team) {
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
        if(newTeam == null) throw new AppException(HttpStatus.BAD_REQUEST ,"Team not found! Please check team ID and try again.");

        // Update properties of the existing team
        if(team.getName() != null) newTeam.setName(team.getName());
        if(team.getWins() != newTeam.getWins()) newTeam.setWins(team.getWins());
        if(team.getDraws() != newTeam.getDraws()) newTeam.setDraws(team.getDraws());
        if(team.getLoses() != newTeam.getLoses()) newTeam.setLoses(team.getLoses());
        if(team.getGoalsForward() != newTeam.getGoalsForward()) newTeam.setGoalsForward(team.getGoalsForward());
        if(team.getGoalsAgainst() != newTeam.getGoalsAgainst()) newTeam.setGoalsAgainst(team.getGoalsAgainst());

        return leagueService.saveLeague(league);
    }

    @Override
    public void deleteTeam(Long teamId) {
        Optional<Team> team = teamRepo.findById(teamId);
        team.ifPresentOrElse(teamRepo::delete, () -> {
            throw new AppException(HttpStatus.BAD_REQUEST ,"League not found! Please check league ID and try again.");
        });
    }
}
