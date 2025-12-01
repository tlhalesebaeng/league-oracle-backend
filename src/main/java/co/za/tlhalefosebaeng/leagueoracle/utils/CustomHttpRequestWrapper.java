package co.za.tlhalefosebaeng.leagueoracle.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> map = new HashMap<>();

    public CustomHttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void setHeader(String name, String value) {
        map.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String header = map.get(name);
        if(header != null) return header;
        return super.getHeader(name);
    }
}
