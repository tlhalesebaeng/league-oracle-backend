package co.za.tlhalefosebaeng.leagueoracle.config;

import co.za.tlhalefosebaeng.leagueoracle.utils.ProtectedRoutes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppConfig {

    // Register a security filter chain bean to the application context. This will allow us to configure the filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable protection against csrf (cross site request forgery)
        http.csrf(AbstractHttpConfigurer::disable);

        // Disable the default login form provided when the user is unauthenticated
        http.formLogin(AbstractHttpConfigurer::disable);

        // Disable basic plain text http authentication
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Make the application stateless
        http.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Authorize all the routes on the protected routes class and permit all other routes
        http.authorizeHttpRequests(customizer -> customizer
                .requestMatchers(ProtectedRoutes.get().toArray(String[]::new))
                .authenticated()
                .anyRequest()
                .permitAll()
        );
        
        return http.build();
    }
}
