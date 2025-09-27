package at.fhtw.restserver.server.tokenManagement;

import java.util.ArrayList;

public class TokenStore {
    private static final ArrayList<String> tokens = new ArrayList<>();

    public void createEntry(String token) {
        tokens.add(token);
    }

    public String getToken(String token) {
        return tokens.stream().filter(t -> t.equals(token)).findFirst().orElse(null);
    }

    public void removeEntry(String token) {
        tokens.remove(token);
    }
}
