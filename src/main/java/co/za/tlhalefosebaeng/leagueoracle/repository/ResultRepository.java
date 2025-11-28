package co.za.tlhalefosebaeng.leagueoracle.repository;

import co.za.tlhalefosebaeng.leagueoracle.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findAllByLeagueId(Long leagueId);
}
