package co.za.tlhalefosebaeng.leagueoracle.service.jwt;

import co.za.tlhalefosebaeng.leagueoracle.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

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
        // Use the user ID to avoid making the email of the user publicly available
        jwtBuilder.setSubject(String.valueOf(user.getId()));

        jwtBuilder.setIssuedAt(new Date(System.currentTimeMillis())); // Set the time which the token was issued at
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + expirationTime)); // Set the expiration time of the token
        jwtBuilder.signWith(this.getKey(), SignatureAlgorithm.HS256); // Set the key and sign it using the HS256 algorithm

        // Build the token and return it
        return jwtBuilder.compact();
    }
}
