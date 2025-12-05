package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.user.AppUserDetailsService;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
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
public class LeagueService implements LeagueServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeagueService.class);
    private final LeagueRepository leagueRepo;
    private final UserServiceInterface userService;
    private final AppUserDetailsService userDetailsService;
    private final HttpServletRequest request;

    // Method to verify that the logged-in user is the creator of the given league.
    // This method should be used under protected routes only as the logged-in user details are required
    @Override
    public boolean isCreator(League league) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to check the league creator: {} LeagueId {}", correlationId, league.getId());
        String email = league.getCreator().getEmail();
        String username = userDetailsService.getPrincipal().getUsername();
        boolean isCreator = username.equals(email);
        LOGGER.info("Successfully checked the league creator: {} LeagueId {} UserId {} {}", correlationId, league.getCreatedAt(), league.getCreator().getId(), isCreator);
        return isCreator;
    }

    @Override
    public League createLeague(LeagueRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to create league: {} Name {}", correlationId, requestDto.getName());

        // Confirm that the team names are unique
        if(!requestDto.validateTeam()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid details! Teams should have different names");
        }

        League league = new League();
        league.setName(requestDto.getName());

        // Get the details of the logged-in user from the user details service and set the creator of the league
        User user = userService.getUserByEmail(userDetailsService.getPrincipal().getUsername());
        league.setCreator(user);

        for(Team team : requestDto.getTeams()) {
            team.setLeague(league);
        }

        league.setTeams(requestDto.getTeams());
        League dbLeague = leagueRepo.save(league);
        LOGGER.info("Successfully created league: {} LeagueId {}", correlationId, dbLeague.getId());
        return dbLeague;
    }

    @Override
    public League getLeague(Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch league: {} LeagueId {}", correlationId, leagueId);
        Optional<League> dbLeague = leagueRepo.findById(leagueId);
        League league = dbLeague.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "League not found! Please check league ID and try again."));
        LOGGER.info("Successfully fetched league: {} LeagueId {}", correlationId, leagueId); // Log the league id to confirm that the leagues are the same
        return league;
    }

    @Override
    public List<League> getAllLeagues(String name) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch leagues: {} Name {}", correlationId, name);
        List<League> leagues = name == null ? leagueRepo.findAll() : leagueRepo.findAllByLeagueName(name);
        LOGGER.info("Successfully fetched leagues: {} Name {} Leagues {}", correlationId, name, leagues.size());
        return leagues;
    }

    @Override
    public List<League> getMyLeagues() {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to fetch user leagues: {}", correlationId);
        User user = userService.getUserByEmail(userDetailsService.getPrincipal().getUsername());
        List<League> leagues = leagueRepo.findAllByCreatorId(user.getId());
        LOGGER.info("Successfully fetched user leagues: {}  UserId {} Leagues {}", correlationId, user.getId(), leagues.size());
        return leagues;
    }

    @Override
    public League updateLeague(Long leagueId, LeagueRequest requestDto) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to update league: {} LeagueId {}", correlationId, leagueId);
        League dbLeague = this.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to update the league
        if(!this.isCreator(dbLeague)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        dbLeague.setName(requestDto.getName());
        League league = leagueRepo.save(dbLeague);
        LOGGER.info("Successfully updated league: {} LeagueId {}", correlationId, league.getId()); // Log the league id to confirm that the ids are the same
        return league;
    }

    @Override
    public void deleteLeague(Long leagueId) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to delete league: {} LeagueId {}", correlationId, leagueId);
        League dbLeague = this.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete the league
        if(!this.isCreator(dbLeague)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        leagueRepo.delete(dbLeague);
        LOGGER.info("Successfully deleted league: {}", correlationId);
    }

}
