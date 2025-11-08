package co.za.tlhalefosebaeng.leagueoracle.utils;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

@Getter
public class RouteDefinition {
    private String URI;
    private String method;
    private AntPathMatcher matcher;

    public RouteDefinition(String URI, String method) {
        this.URI = URI;
        this.method = method;
        this.matcher = new AntPathMatcher();
    }

    public HttpMethod getMethod() {
        return HttpMethod.valueOf(this.method);
    }

    // Helper method to check if request path and method match ones of a reference route definition
    public boolean matches(String method, String path) {
        boolean methodsMatch = this.method.matches(method); // Convert the method to a string and check if the methods are the same
        boolean pathsMath = matcher.match(this.URI, path); // Check if the path match the URI using the strategy of the matcher
        return  methodsMatch && pathsMath;
    }
}
