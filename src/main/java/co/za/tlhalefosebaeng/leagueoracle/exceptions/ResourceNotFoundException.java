package co.za.tlhalefosebaeng.leagueoracle.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private HttpStatus code;
    public ResourceNotFoundException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }
}
