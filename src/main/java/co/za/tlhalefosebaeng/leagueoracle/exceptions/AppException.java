package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
