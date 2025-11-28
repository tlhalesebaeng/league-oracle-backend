package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;

import java.util.List;

public interface FixtureServiceInterface {
    List<Fixture> generateLeagueFixtures(Long leagueId);
    List<Fixture> getAllLeagueFixtures(Long leagueId);
    Fixture getFixture(Long fixtureId);
    Fixture updateFixture(Long fixtureId, UpdateFixtureRequest requestDto);
    void deleteFixture(Long fixtureId);
    List<Fixture> getUpcomingFixtures(Integer month);
}
