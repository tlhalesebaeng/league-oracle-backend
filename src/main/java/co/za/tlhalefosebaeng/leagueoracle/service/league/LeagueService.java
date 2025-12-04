package co.za.tlhalefosebaeng.leagueoracle.service.league;

import co.za.tlhalefosebaeng.leagueoracle.dto.league.LeagueRequest;
import co.za.tlhalefosebaeng.leagueoracle.exceptions.AppException;
import co.za.tlhalefosebaeng.leagueoracle.entity.League;
import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.user.AppUserDetailsService;
import co.za.tlhalefosebaeng.leagueoracle.service.user.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeagueService implements LeagueServiceInterface {
    private final LeagueRepository leagueRepo;
    private final UserServiceInterface userService;
    private final AppUserDetailsService userDetailsService;

    // Method to verify that the logged-in user is the creator of the given league.
    // This method should be used under protected routes only as the logged-in user details are required
    @Override
    public boolean isCreator(League league) {
        String email = league.getCreator().getEmail(); // Get the email of the leagues creator
        String username = userDetailsService.getPrincipal().getUsername(); // Get the username of the principal user
        return username.equals(email); // Emails are unique so if they match then the principal user is the creator of the league
    }

    @Override
    public League createLeague(LeagueRequest requestDto) {
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
        return leagueRepo.save(league);
    }

    @Override
    public League getLeague(Long leagueId) {
        Optional<League> league = leagueRepo.findById(leagueId);
        return league.orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "League not found! Please check league ID and try again."));
    }

    @Override
    public List<League> getAllLeagues(String name) {
        if(name != null) return leagueRepo.findAllByLeagueName(name);
        return leagueRepo.findAll();
    }

    @Override
    public List<League> getMyLeagues() {
        // Get the logged-in user
        User user = userService.getUserByEmail(userDetailsService.getPrincipal().getUsername());

        // Find all the leagues that have a creator that's similar to the logged-in user
        return leagueRepo.findAllByCreatorId(user.getId());
    }

    @Override
    public League updateLeague(Long leagueId, LeagueRequest requestDto) {
        League leagueToUpdate = this.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to update the league
        if(!this.isCreator(leagueToUpdate)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        leagueToUpdate.setName(requestDto.getName());
        return leagueRepo.save(leagueToUpdate);
    }

    @Override
    public void deleteLeague(Long leagueId) {
        League leagueToDelete = this.getLeague(leagueId);

        // Confirm the creator of this league - Only logged-in creator of league should be able to delete the league
        if(!this.isCreator(leagueToDelete)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "You have to be the league creator to perform this operation");
        }

        leagueRepo.delete(leagueToDelete);
    }

}
