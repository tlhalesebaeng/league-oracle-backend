package co.za.tlhalefosebaeng.leagueoracle.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class RouteDefinition {
    private String uri;
    private HttpStatus method;
}
