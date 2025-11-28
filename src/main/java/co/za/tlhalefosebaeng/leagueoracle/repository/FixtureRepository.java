package co.za.tlhalefosebaeng.leagueoracle.repository;

import co.za.tlhalefosebaeng.leagueoracle.entity.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    List<Fixture> findAllByLeagueId(Long leagueId);
}
