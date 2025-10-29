# MRP Documentation

## Controller

In den Controllern wurden Annotations verwendet, 
um den Pfad, REST-Methode und ob eine Authentifizierung benötigt wird zu definieren. <br>
Damit die Annotation zur Laufzeit in den Controller Klassen gescannt und erkannt werden, 
habe ich einen ControllerProcessor geschrieben.

```
@Controller(path = "/api/media", method = Method.POST)
public static void create(Request request) {...}
```

## RestService

Das RestService ist das einzige Service des REST Servers und leitet die jeweiligen Anfragen <br>
je nach Pfad der Anfrage zu der jeweiligen Methode weiter.
Sollte der angeforderte Pfad nicht verfügbar sein, wird ein 404 Error zurückgeschickt.

## Authentifizierung

Für die Authentifizierung habe ich mir einen Tokenmanager geschrieben.<br>
Dieser ist als Singleton mit der Enum Methode implementiert, damit er nur einmal existiert.

Der TokenManager erstellt, verifiziert und terminiert den Token. Wenn der User sich anmeldet, <br>
wird ein neuer Token erstellt und der TokenManager speichert diesen in seinem TokenStore.

Der TokenStore selbst ist nur für das Speichern, Löschen und Abfragen der gespeicherten Tokens zuständig.<br>
Er ist ebenfalls als Singleton implementiert.

Wenn eine Anfrage zur REST API geschickt wird, wird der mitgelieferte Token im RestService in der Methode handle <br>
durch den TokenManager verifiziert. Sollte die Validierung fehlschlagen, wird ein 403 Error zurückgeschickt und die Anfrage nicht weiter bearbeitet.

```
public enum TokenManager {
    INSTANCE(TokenStore.INSTANCE);
    private final TokenStore tokenStore;

    TokenManager(TokenStore tokenStore) {...}

    public String createToken(User user) {...}

    public void terminateToken(String tokenString) {...}

    public Token getToken(String tokenString) {...}

    public Integer getCurrentUserId(String tokenString) {...}

    private static String generateJWT(User user) {...}

    public Boolean isVerified(String tokenString) {...}
}

public enum TokenStore {
    INSTANCE(new ArrayList<>());

    private final ArrayList<Token> tokens;

    TokenStore(ArrayList<Token> tokens) {...}

    public void createEntry(Token token) {...}

    public Token getToken(String tokenString) {...}

    public void removeEntry(String tokenString) {...}
}
```

## Logging

Um die möglichen Exceptions oder wichtigen Informationen im Nachhinein noch lesen zu können, habe ich einen Logger geschrieben. <br>
Dieser kontrolliert zuerst, ob der Ordner LogFiles vorhanden ist. Wenn nicht, wird dieser erstellt. <br>
Als Nächstes wird, sofern nicht vorhanden, ein LogFile mit dem heutigen Datum angelegt. <br>
Um den Logtypen zu spezifizieren, wurde ein Enum geschrieben, wodurch man festlegen kann, ob ein Log eine Info oder Error ist.

#### Nutzung
```
Logger.log(LogType.ERROR, "Failed to get media: " + e.getLocalizedMessage());
```