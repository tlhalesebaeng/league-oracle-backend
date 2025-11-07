package co.za.tlhalefosebaeng.leagueoracle.service.jwt;

import co.za.tlhalefosebaeng.leagueoracle.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtServiceInterface {
    String generateToken(User user);
    boolean validateToken(String token, UserDetails userDetails);
    Claims getAllClaims(String token);
}
