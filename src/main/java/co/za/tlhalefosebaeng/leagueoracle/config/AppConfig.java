package co.za.tlhalefosebaeng.leagueoracle.config;

import co.za.tlhalefosebaeng.leagueoracle.filters.JwtFilter;
import co.za.tlhalefosebaeng.leagueoracle.utils.ProtectedRoutes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class AppConfig {
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final DelegatedAuthenticationEntryPoint authEntryPoint;

    @Value("${api.password-encoder.strength}")
    private Integer encoderStrength;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(encoderStrength);
    }

    // Register an authentication provider bean to the authentication manager
    public AuthenticationProvider authProvider() {
        // Instantiate a new DAO (Data Access Object) provider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        // Set the password encoder. This helps with password encryption and decryption
        provider.setPasswordEncoder(passwordEncoder());

        // Return the instance of the provider
        return provider;
    }

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
        http.authorizeHttpRequests(customizer -> {
            ProtectedRoutes.get().forEach(
                    route -> customizer.requestMatchers(route.getMethod(), route.getURI()).authenticated()
            );
            customizer.anyRequest().permitAll();
        });

        // Add the jwt filter before the filter that extracts the username and password from the request
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Allow exception handling and use the delegated authentication entry point as the authentication entry point
        http.exceptionHandling(customizer -> customizer.authenticationEntryPoint(authEntryPoint));

        // Set the custom authentication provider
        http.authenticationProvider(authProvider());
        
        return http.build();
    }
}
