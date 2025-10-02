package at.fhtw.mrp.dal.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public class User {
    private final Integer userId;
    @Setter @NonNull
    private String username;
    @Setter @NonNull
    private String password;

    public User(@NonNull Integer userId, @NonNull String username, @NonNull String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}

