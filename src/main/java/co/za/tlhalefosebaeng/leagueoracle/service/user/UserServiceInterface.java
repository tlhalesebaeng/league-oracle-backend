package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.model.User;

public interface UserServiceInterface {
    User getUserByEmail(String email);
}
