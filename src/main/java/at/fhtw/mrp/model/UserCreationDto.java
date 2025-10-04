package at.fhtw.mrp.model;

import at.fhtw.mrp.utils.PasswordHashManager;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@AllArgsConstructor
public class UserCreationDto {
    @JsonAlias({"username"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @Getter @NonNull
    private String username;
    @JsonAlias({"password"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @Getter @NonNull
    private String password;

    public UserCreationDto() {}

    public void hashPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PasswordHashManager passwordHashManager = new PasswordHashManager();
        this.password = Objects.requireNonNull(passwordHashManager.hashPassword(this.password));
    }
}
