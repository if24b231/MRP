package at.fhtw.mrp.model;

import at.fhtw.mrp.utils.PasswordHashManager;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@Getter
@Builder
@Jacksonized
public class UserCreationDto {
    @JsonAlias({"username"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @NonNull
    private String username;
    @JsonAlias({"password"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @NonNull
    private String password;

    public void hashPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PasswordHashManager passwordHashManager = new PasswordHashManager();
        this.password = Objects.requireNonNull(passwordHashManager.hashPassword(this.password));
    }
}
