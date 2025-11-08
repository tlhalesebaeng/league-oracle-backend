package co.za.tlhalefosebaeng.leagueoracle.filters;

import co.za.tlhalefosebaeng.leagueoracle.service.jwt.JwtServiceInterface;
import co.za.tlhalefosebaeng.leagueoracle.service.user.AppUserDetailsService;
import co.za.tlhalefosebaeng.leagueoracle.utils.ProtectedRoutes;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtServiceInterface jwtService;
    private final AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Do not perform this filter for routes that are not protected
        if(!ProtectedRoutes.get().contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get the token from the cookie
        String token = null;
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("access_jwt")) token = cookie.getValue();
            }
        }

        // Get the username from the token
        String username = null;
        if(token != null) username = jwtService.getAllClaims(token).getSubject();

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get the user from the database using the username. This will confirm that the user exists
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token and add the user to the security context holder
            if(jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Perform the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
