package co.za.tlhalefosebaeng.leagueoracle.service.jwt;

import co.za.tlhalefosebaeng.leagueoracle.model.User;
import co.za.tlhalefosebaeng.leagueoracle.service.user.AppUserDetails;

public interface JwtServiceInterface {
    String generateToken(User user);
    boolean validateToken(String token, AppUserDetails userDetails);
}
