package at.fhtw.restserver.http;

public enum ContentType {
    JSON("application/json");

    public final String value;

    ContentType(String value) {this.value = value;}
}
