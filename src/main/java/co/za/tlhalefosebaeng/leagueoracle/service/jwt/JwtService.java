package co.za.tlhalefosebaeng.leagueoracle.service.jwt;

import co.za.tlhalefosebaeng.leagueoracle.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService implements JwtServiceInterface {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-time}")
    private Long expirationTime;

    // Intermediate method to get the secret key of the token
    private Key getKey() {
        // Get the base 64 key bytes
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        // Create a secret key instance based on the key bytes and return it
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User user) {
        // Instantiate a new instance of jwt builder which is what will be used to build the token
        JwtBuilder jwtBuilder = Jwts.builder();

        // Set the subject of the token which is what is used to identify a user
        jwtBuilder.setSubject(user.getEmail());

        jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis())); // Set the time which the token was issued at
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + expirationTime)); // Set the expiration time of the token
        jwtBuilder.signWith(this.getKey(), SignatureAlgorithm.HS256); // Set the key and sign it using the HS256 algorithm

        // Build the token and return it
        return jwtBuilder.compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        // Get all the claims from the token
        Claims claims = getAllClaims(token);

        // Extract the subject from all the claims check if the username from the user details is similar to it
        boolean usernamesMatch = userDetails.getUsername().equals(claims.getSubject());

        // Extract the token expiration and check that it's before the current date
        boolean tokenNotExpired = claims.getExpiration().before(new Date());

        // Get the results of the two above checks and return it
        return usernamesMatch && tokenNotExpired;
    }

    @Override
    public Claims getAllClaims(String token) {
        // Instantiate a new instance of jwt parser builder and set the signing key which will be used to decode the token
        JwtParserBuilder jwtBuilder = Jwts.parserBuilder();
        jwtBuilder.setSigningKey(this.getKey());

        // Get an instance of what allows us to convert the jwt to a readable format and use it to return the claims
        JwtParser jwtParser = jwtBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
