package co.za.tlhalefosebaeng.leagueoracle.config;

import co.za.tlhalefosebaeng.leagueoracle.filters.JwtFilter;
import co.za.tlhalefosebaeng.leagueoracle.filters.RequestResponseLoggerFilter;
import co.za.tlhalefosebaeng.leagueoracle.service.routes.RoutesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class AppConfig {
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final RequestResponseLoggerFilter loggerFilter;
    private final DelegatedAuthenticationEntryPoint authEntryPoint;
    private final RoutesService routesService;
    private final @Qualifier("customCorsConfiguration") CorsConfigurationSource configSource;

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

        // Use the custom cors configuration class to customise cors
        http.cors(customizer -> customizer.configurationSource(configSource));

        // Make the application stateless
        http.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Authorize all the routes on the protected routes class and permit all other routes
        http.authorizeHttpRequests(customizer -> {
            routesService.getProtected().forEach(
                    route -> customizer.requestMatchers(route.getMethod(), route.getURI()).authenticated()
            );
            customizer.anyRequest().permitAll();
        });

        // Add the custom logging filter before the second spring security filter (WebAsyncManager)
        http.addFilterBefore(loggerFilter, WebAsyncManagerIntegrationFilter.class);

        // Add the jwt filter before the filter that extracts the username and password from the request
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Allow exception handling and use the delegated authentication entry point as the authentication entry point
        http.exceptionHandling(customizer -> customizer.authenticationEntryPoint(authEntryPoint));

        // Set the custom authentication provider
        http.authenticationProvider(authProvider());
        
        return http.build();
    }
}
