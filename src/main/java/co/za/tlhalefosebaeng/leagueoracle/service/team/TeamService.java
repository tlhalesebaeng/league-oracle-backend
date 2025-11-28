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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamServiceInterface{
    private final LeagueServiceInterface leagueService;
    private final TeamRepository teamRepo;

    @Override
    public Team addTeam(Long leagueId, AddTeamRequest requestDto) {
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

        Team team = new Team();
        team.setLeague(league);
        team.setName(requestDto.getName());

        return teamRepo.save(team);
    }

    @Override
    public Team getTeam(Long teamId) {
        Optional<Team> team = teamRepo.findById(teamId);
        return team.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST ,"Team not found! Please check team ID and try again"));
    }

    @Override
    public Team updateTeam(Long teamId, UpdateTeamRequest requestDto) {
        Team team = this.getTeam(teamId);
        League league = leagueService.getLeague(team.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to update teams of a league
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // To increase the integrity of the system, only the team name can be updated. Other team fields should be updated through results
        if(requestDto.getName() != null) team.setName(requestDto.getName());

        return teamRepo.save(team);
    }

    @Override
    public void deleteTeam(Long teamId) {
        Team team = this.getTeam(teamId);
        League league = leagueService.getLeague(team.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete a team
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        teamRepo.delete(team);
    }
}
