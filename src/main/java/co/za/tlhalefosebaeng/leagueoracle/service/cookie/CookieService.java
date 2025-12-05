package co.za.tlhalefosebaeng.leagueoracle.service.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CookieService implements CookieServiceInterface{
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieService.class);
    private final HttpServletRequest request;

    @Value("${api.environment}")
    private String environment;

    // Helper method that creates a cookie with all the necessary configuration
    @Override
    public Cookie create(String name, String value) {
        String correlationId = (String) request.getAttribute("correlation-id");
        LOGGER.info("Preparing to create cookie: {} Name {}", correlationId,  name);

        Cookie cookie = new Cookie(name, value);
        cookie.isHttpOnly();
        cookie.setPath("/"); // Set the URL which the browser should be under for it to send the cookie

        LOGGER.info("Cookie created successfully: {} Name {}", correlationId,  name);

        return cookie;
    }
}
