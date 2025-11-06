package co.za.tlhalefosebaeng.leagueoracle.exceptions;

public class PasswordsNotMatchingException extends RuntimeException{
    public PasswordsNotMatchingException(String message) {
        super(message);
    }
}
