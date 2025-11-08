package co.za.tlhalefosebaeng.leagueoracle.utils;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class RouteDefinition {
    private String uri;
    private HttpStatus method;
}
