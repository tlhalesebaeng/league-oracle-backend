package co.za.tlhalefosebaeng.leagueoracle.service.jwt;

import co.za.tlhalefosebaeng.leagueoracle.model.User;

public interface JwtServiceInterface {
    String generateToken(User user);
}
