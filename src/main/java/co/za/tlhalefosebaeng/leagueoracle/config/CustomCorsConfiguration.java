package co.za.tlhalefosebaeng.leagueoracle.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CustomCorsConfiguration implements CorsConfigurationSource {
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        // Create a new instance of the cors configuration class
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowCredentials(true); // Allow cookies and other credentials to be included
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173")); // Set the allowed domains
        corsConfig.setAllowedMethods(List.of("*")); // Allow all http request methods
        corsConfig.setAllowedHeaders(List.of("*")); // Allow all request headers

        // Return the configuration instance
        return corsConfig;
    }
}
