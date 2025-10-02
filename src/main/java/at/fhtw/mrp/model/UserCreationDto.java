package at.fhtw.mrp.model;

import at.fhtw.mrp.model.exceptions.ValidationException;
import at.fhtw.mrp.utils.PasswordHashManager;
import at.fhtw.mrp.utils.deserializer.UserCreationDtoDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@JsonDeserialize(using = UserCreationDtoDeserializer.class)
public class UserCreationDto {
    @JsonAlias({"username"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @Getter
    private String username;
    @JsonAlias({"password"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @Getter
    private String password;

    public UserCreationDto(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    public void hashPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PasswordHashManager passwordHashManager = new PasswordHashManager();
        this.password = passwordHashManager.hashPassword(this.password);
    }
}
