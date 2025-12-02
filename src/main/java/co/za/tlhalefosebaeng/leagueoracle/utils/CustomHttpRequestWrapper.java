package co.za.tlhalefosebaeng.leagueoracle.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders;

    public CustomHttpRequestWrapper(HttpServletRequest request) {
        super(request);
         this.customHeaders = new HashMap<>();
    }

    public void setHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String header = customHeaders.get(name);
        if(header != null) return header;
        return super.getHeader(name);
    }
}
