package co.za.tlhalefosebaeng.leagueoracle.response;

import lombok.Data;

@Data
public class ApiResponse {
    private String message;
    private Object data;
}
