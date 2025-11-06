package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PasswordsNotMatchingException extends RuntimeException{
    private HttpStatus code;
    public PasswordsNotMatchingException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST;
    }
}
