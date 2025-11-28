package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
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

    @Override
    public League addTeam(Long leagueId, AddTeamRequest requestDto) {
        // Get the league from the database using the league service
        League league = leagueService.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to add teams of a league
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // Check if the league has fixtures. If it does we should not be able to add any team
        if(!league.getFixtures().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot add teams anymore! This league already has started");
        }

        // Confirm that the league does not have a team that has a name similar to the add team request dto name
        for(Team t : league.getTeams()) {
            if(t.getName().equals(requestDto.getName())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Team names should be unique! Please check your team name and try again");
            }
        }

        // Set the necessary team properties
        Team newTeam = new Team();
        newTeam.setLeague(league);
        newTeam.setName(requestDto.getName());

        // Add the team to the list of teams and return the newly saved league
        league.getTeams().add(newTeam);
        return leagueService.saveLeague(league);
    }

    @Override
    public Team getTeam(Long teamId) {
        Optional<Team> team = teamRepo.findById(teamId);
        return team.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST ,"Team not found! Please check team ID and try again"));
    }

    @Override
    public League updateTeam(Long leagueId, Long teamId, UpdateTeamRequest requestDto) {
        // Get the league from the database using the league service
        League league = leagueService.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to update teams of a league
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

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
        if(requestDto.getName() != null) newTeam.setName(requestDto.getName());
        if(requestDto.getWins() != newTeam.getWins()) newTeam.setWins(requestDto.getWins());
        if(requestDto.getDraws() != newTeam.getDraws()) newTeam.setDraws(requestDto.getDraws());
        if(requestDto.getLoses() != newTeam.getLoses()) newTeam.setLoses(requestDto.getLoses());
        if(requestDto.getGoalsForward() != newTeam.getGoalsForward()) newTeam.setGoalsForward(requestDto.getGoalsForward());
        if(requestDto.getGoalsAgainst() != newTeam.getGoalsAgainst()) newTeam.setGoalsAgainst(requestDto.getGoalsAgainst());

        return leagueService.saveLeague(league);
    }

    @Override
    public void deleteTeam(Long teamId) {
        // Get the team from the database using the team repo otherwise throw the relevant exception
        Team team = teamRepo.findById(teamId).orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST ,"Team not found! Please check team ID and try again"));

        // Get the league with the team league's id from the database using the league service
        League league = leagueService.getLeague(team.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete a team
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        teamRepo.delete(team); // Delete the team using the repo
    }
}
