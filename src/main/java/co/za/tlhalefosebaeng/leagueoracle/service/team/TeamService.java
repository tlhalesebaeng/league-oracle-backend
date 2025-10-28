package co.za.tlhalefosebaeng.leagueoracle.service.team;

import co.za.tlhalefosebaeng.leagueoracle.dto.team.AddTeamRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.League;
import co.za.tlhalefosebaeng.leagueoracle.model.Team;
import co.za.tlhalefosebaeng.leagueoracle.repository.LeagueRepository;
import co.za.tlhalefosebaeng.leagueoracle.service.league.LeagueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
