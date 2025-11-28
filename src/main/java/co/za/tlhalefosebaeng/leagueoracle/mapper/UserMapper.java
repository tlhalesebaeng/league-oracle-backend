package co.za.tlhalefosebaeng.leagueoracle.mapper;

import co.za.tlhalefosebaeng.leagueoracle.dto.auth.UserResponse;
import co.za.tlhalefosebaeng.leagueoracle.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());

        return userResponse;
    }
}
