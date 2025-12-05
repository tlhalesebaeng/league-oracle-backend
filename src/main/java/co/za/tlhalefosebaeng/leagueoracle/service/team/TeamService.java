package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.team.UpdateTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.TeamRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamServiceInterface{
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamService.class);
    private final LeagueServiceInterface leagueService;
    private final TeamRepository teamRepo;
    private final HttpServletRequest request;

    @Override
    public Team addTeam(Long leagueId, AddTeamRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to add team: {} LeagueId {}", correlationId, leagueId);
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

        Team dbTeam = teamRepo.save(team);
        LOGGER.info("Successfully added team: {} TeamId {}", correlationId, dbTeam.getId());
        return dbTeam;
    }

    @Override
    public Team getTeam(Long teamId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch team: {} TeamId {}", correlationId, teamId);
        Optional<Team> dbTeam = teamRepo.findById(teamId);
        Team team = dbTeam.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Team not found! Please check team ID and try again"));
        LOGGER.info("Successfully fetched team: {} TeamId {}", correlationId, team.getId()); // Log the team id to confirm that the ids are similar
        return team;
    }

    @Override
    public Team updateTeam(Long teamId, UpdateTeamRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to update team: {} TeamId {}", correlationId, teamId);
        Team dbTeam = this.getTeam(teamId);
        League league = leagueService.getLeague(dbTeam.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to update teams of a league
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        // To increase the integrity of the system, only the team name can be updated. Other team fields should be updated through results
        if(requestDto.getName() != null) dbTeam.setName(requestDto.getName());

        Team team = teamRepo.save(dbTeam);
        LOGGER.info("Successfully updated team: {} TeamId {}", correlationId, team.getId()); // Log the team id to confirm that the ids are similar
        return team;
    }

    @Override
    public List<Team> getAllLeagueTeams(Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch all league teams: {} LeagueId {}", correlationId, leagueId);
        League league = leagueService.getLeague(leagueId);
        List<Team> teams = teamRepo.findAllByLeagueId(league.getId());
        LOGGER.info("Successfully fetched all league teams: {} LeagueId {} Teams {}", correlationId, leagueId, teams.size());
        return teams;
    }

    @Override
    public void deleteTeam(Long teamId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to delete team: {} TeamId {}", correlationId, teamId);
        Team team = this.getTeam(teamId);
        League league = leagueService.getLeague(team.getLeague().getId());

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete a team
        if(!leagueService.isCreator(league)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        teamRepo.delete(team);
        LOGGER.info("Successfully deleted team: {}", correlationId);
    }
}
