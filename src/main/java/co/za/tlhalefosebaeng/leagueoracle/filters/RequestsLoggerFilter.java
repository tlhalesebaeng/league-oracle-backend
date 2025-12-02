package co.za.tlhalefosebaeng.leagueoracle.filters;

import co.za.tlhalefosebaeng.leagueoracle.utils.CustomHttpRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestsLoggerFilter extends OncePerRequestFilter {
    private static final Logger httpLogger = LoggerFactory.getLogger(RequestsLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CustomHttpRequestWrapper httpRequest = new CustomHttpRequestWrapper(request);

        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String query = queryString != null ? "?" + queryString : "";
        String correlationId = UUID.randomUUID().toString();

        httpRequest.setHeader("X-Correlation-Id", correlationId);

        httpLogger.info("Incoming: {} {} {}{}",correlationId, method, uri, query);

        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long total = System.currentTimeMillis() - start;

        int status = response.getStatus();
        httpLogger.info("Outgoing: {} HTTP {} {} ms", correlationId, status, total);
    }
}
