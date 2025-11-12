package co.za.tlhalefosebaeng.leagueoracle.service.cookie;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService implements CookieServiceInterface{
    @Value("${api.environment}")
    private String environment;

    // Helper method that creates a cookie with all the necessary configuration
    @Override
    public Cookie create(String name, String value) {
        Cookie cookie = new Cookie(name, value); // Create a new instance of a cookie
        cookie.isHttpOnly(); // Make the cookie http only
        cookie.setPath("/"); // Set the URL which the browser should be under for it to send the cookie

        // When the server environment is not development make the cookie secure
        if(!environment.equals("development")) cookie.setSecure(true);

        // When all configuration are done return the cookie
        return cookie;
    }
}
