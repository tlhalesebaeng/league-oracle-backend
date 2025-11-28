package co.za.tlhalefosebaeng.leagueoracle.repository;

import co.za.tlhalefosebaeng.leagueoracle.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByLeagueId(Long id);
}
