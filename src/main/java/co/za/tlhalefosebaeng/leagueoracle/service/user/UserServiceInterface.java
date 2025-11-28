package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.LoginRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;

public interface UserServiceInterface {
    User getUserByEmail(String email);
    User addUser(SignupRequest user);
    User login(LoginRequest details);
    User checkAuth(String jwt);
}
