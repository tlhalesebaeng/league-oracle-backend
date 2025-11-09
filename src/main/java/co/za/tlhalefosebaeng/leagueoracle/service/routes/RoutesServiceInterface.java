package co.za.tlhalefosebaeng.leagueoracle.service.routes;

import co.za.tlhalefosebaeng.leagueoracle.utils.RouteDefinition;

import java.util.List;

public interface RoutesServiceInterface {
    List<RouteDefinition> getProtected();
    boolean isProtected(String method, String URI);
}
