package co.za.tlhalefosebaeng.leagueoracle.service.routes;

import co.za.tlhalefosebaeng.leagueoracle.utils.RouteDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutesService implements RoutesServiceInterface {
    @Value("${api.endpoint.prefix}")
    private String prefix;

    @Override
    public List<RouteDefinition> getProtected() {
        // Return an unmodifiable list of routes that should be authorized
        return List.of(
                // League Routes
                new RouteDefinition(prefix + "/leagues", "POST"),
                new RouteDefinition(prefix + "/leagues/mine", "GET"),
                new RouteDefinition(prefix + "/leagues/{leagueId}", "PATCH"),
                new RouteDefinition(prefix + "/leagues/{leagueId}", "DELETE"),

                // Team Routes
                new RouteDefinition(prefix + "/teams", "POST"),
                new RouteDefinition(prefix + "/teams/{teamId}", "PATCH"),
                new RouteDefinition(prefix + "/teams/{teamId}", "DELETE"),

                // Fixture Routes
                new RouteDefinition(prefix + "/fixtures", "POST"),
                new RouteDefinition(prefix + "/fixtures/upcoming", "GET"),
                new RouteDefinition(prefix + "/fixtures/{fixtureId}", "PATCH"),
                new RouteDefinition(prefix + "/fixtures/{fixtureId}", "DELETE"),

                // Result Routes
                new RouteDefinition(prefix + "/results", "POST"),
                new RouteDefinition(prefix + "/results/{resultId}", "PATCH")
        );
    }

    // Helper method to check if a route is part of the protected routes
    @Override
    public boolean isProtected(String method, String URI) {
        for(RouteDefinition route : this.getProtected()) {
            // Check if the route is part of the protected routes. The request should have similar URI and http method as the protected route
            if(route.matches(method, URI)){
                return true; // The route is found so it is protected
            }
        }

        // All checks are successful, the request is not protected
        return false;
    }
}
