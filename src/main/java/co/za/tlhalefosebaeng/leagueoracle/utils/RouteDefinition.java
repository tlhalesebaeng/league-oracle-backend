package co.za.tlhalefosebaeng.leagueoracle.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@AllArgsConstructor
public class RouteDefinition {
    private String URI;
    private String method;

    public HttpMethod getMethod() {
        return HttpMethod.valueOf(this.method);
    }
}
