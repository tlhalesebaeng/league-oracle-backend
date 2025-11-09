package co.za.tlhalefosebaeng.leagueoracle.service.cookie;

import jakarta.servlet.http.Cookie;

public interface CookieServiceInterface {
    Cookie create(String name, String value);
}
