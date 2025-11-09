package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
