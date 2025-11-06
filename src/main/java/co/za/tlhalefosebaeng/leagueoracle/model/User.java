package co.za.tlhalefosebaeng.leagueoracle.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    private String fullName;
    private String lastName;
    private String email;
    private String password;
}
