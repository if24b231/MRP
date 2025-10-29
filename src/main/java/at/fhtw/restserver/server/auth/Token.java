package at.fhtw.restserver.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
public class Token {
    @NonNull
    String tokenString;
    @NonNull
    LocalDateTime createdAt;
    @NonNull
    LocalDateTime lastVerified;
}
