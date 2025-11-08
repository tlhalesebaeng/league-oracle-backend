package co.za.tlhalefosebaeng.leagueoracle.utils;

import java.util.List;

public class Routes {
    public static List<RouteDefinition> getProtected() {
        // Return an unmodifiable list of routes that should be authorized
        return List.of(
                // League Routes
                new RouteDefinition("/api/v1/leagues", "POST"),
                new RouteDefinition("/api/v1/leagues/{leagueId}", "PATCH"),
                new RouteDefinition("/api/v1/leagues/{leagueId}", "DELETE"),

                // Team Routes
                new RouteDefinition("/api/v1/teams", "POST"),
                new RouteDefinition("/api/v1/teams/{teamId}", "PATCH"),
                new RouteDefinition("/api/v1/teams/{teamId}", "DELETE"),

                // Fixture Routes
                new RouteDefinition("/api/v1/fixtures", "POST"),
                new RouteDefinition("/api/v1/fixtures/{fixtureId}", "PATCH"),
                new RouteDefinition("/api/v1/fixtures/{fixtureId}", "DELETE"),

                // Result Routes
                new RouteDefinition("/api/v1/results", "POST"),
                new RouteDefinition("/api/v1/results/{resultId}", "POST")
        );
    }

    // Helper method to check if a route is part of the protected routes
    public static boolean isProtected(String method, String URI) {
        for(RouteDefinition route : getProtected()) {
            // Check if the route is part of the protected routes. The request should have similar URI and http method as the protected route
            if(route.matches(method, URI)){
                return true; // The route is found so it is protected
            }
        }

        // All checks are successful, the request is not protected
        return false;
    }
}
