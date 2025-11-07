package co.za.tlhalefosebaeng.leagueoracle.service.user;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.SignupRequest;
import co.za.tlhalefosebaeng.leagueoracle.dto.auth.UserResponse;
import co.za.tlhalefosebaeng.leagueoracle.model.User;

public interface UserServiceInterface {
    User getUserByEmail(String email);
    User addUser(SignupRequest user);
    UserResponse convertUserToDto(User user);
}
