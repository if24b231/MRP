package at.fhtw.restserver.server.auth;

import java.util.ArrayList;

public enum TokenStore {
    INSTANCE(new ArrayList<>());

    private final ArrayList<Token> tokens;

    TokenStore(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public void createEntry(Token token) {
        tokens.add(token);
    }

    public Token getToken(String tokenString) {
        return tokens.stream().filter(t -> t.tokenString.equals(tokenString)).findFirst().orElse(null);
    }

    public void removeEntry(String tokenString) {
        Token token = getToken(tokenString);
        tokens.remove(token);
    }
}
