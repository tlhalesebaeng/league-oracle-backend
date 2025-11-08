package co.za.tlhalefosebaeng.leagueoracle.utils;

import java.util.List;

public class Routes {
    public static List<RouteDefinition> getProtected() {
        // Return an unmodifiable list of routes that should be authorized
        return List.of();
    }

    // Helper method to check if a route is part of the protected routes
    public static boolean isProtected(String method, String URI) {
        for(RouteDefinition route : getProtected()) {
            // Check if the route is part of the protected routes. The request should have similar URI and http method as the protected route
            if(route.getMethod().matches(method) && route.getURI().equals(URI)){
                return true; // The route is found so it is protected
            }
        }

        // All checks are successful, the request is not protected
        return false;
    }
}
