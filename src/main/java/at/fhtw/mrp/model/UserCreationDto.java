package at.fhtw.mrp.model;

import at.fhtw.mrp.utils.PasswordHashManager;
import at.fhtw.mrp.utils.deserializer.UserCreationDtoDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@JsonDeserialize(using = UserCreationDtoDeserializer.class)
public class UserCreationDto {
    @JsonAlias({"username"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String username;
    @JsonAlias({"password"})
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String password;

    public UserCreationDto() {}

    public UserCreationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {return password;}
    public void hashPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PasswordHashManager passwordHashManager = new PasswordHashManager();
        this.password = passwordHashManager.hashPassword(this.password);
    }
}
