package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private HttpStatus code;
    public ResourceNotFoundException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST;
    }
}
