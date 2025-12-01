package co.za.tlhalefosebaeng.leagueoracle.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestResponseLoggerFilter extends OncePerRequestFilter {
    private static final Logger httpLogger = LoggerFactory.getLogger(RequestResponseLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String query = queryString != null ? "?" + queryString : "";

        httpLogger.info("Incoming: {} {}{}", method, uri, query);

        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long total = System.currentTimeMillis() - start;

        int status = response.getStatus();
        httpLogger.info("Outgoing: HTTP {} {} ms", status, total);
    }
}
