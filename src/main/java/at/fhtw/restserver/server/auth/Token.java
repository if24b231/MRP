package at.fhtw.restserver.server.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor
public class Token {
    String tokenString;
    LocalDateTime createdAt;
    LocalDateTime lastVerified;
}
