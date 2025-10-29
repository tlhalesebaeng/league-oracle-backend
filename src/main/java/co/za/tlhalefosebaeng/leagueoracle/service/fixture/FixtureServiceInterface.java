package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;

import java.util.List;

public interface FixtureServiceInterface {
    List<Fixture> generateLeagueFixtures(Long leagueId);
}
