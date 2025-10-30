package co.za.tlhalefosebaeng.leagueoracle.service.fixture;

import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.FixtureResponse;
import co.za.tlhalefosebaeng.leagueoracle.dto.fixture.UpdateFixtureRequest;
import co.za.tlhalefosebaeng.leagueoracle.model.Fixture;

import java.util.List;

public interface FixtureServiceInterface {
    List<Fixture> generateLeagueFixtures(Long leagueId);
    FixtureResponse convertFixtureToDto(Fixture fixture);
    List<Fixture> getAllLeagueFixtures(Long leagueId);
    Fixture getLeagueById(Long fixtureId);
    Fixture updateFixture(Long fixtureId, UpdateFixtureRequest fixtureRequest);
}
