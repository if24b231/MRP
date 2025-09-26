package at.fhtw.mrp.utils.deserializer;

import at.fhtw.mrp.model.UserCreationDto;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;

public class UserCreationDeserializer extends StdDeserializer<UserCreationDto> {

    public UserCreationDeserializer() {
        this(null);
    }

    public UserCreationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UserCreationDto deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        String username = node.get("username").asText();
        String password = node.get("password").asText();
        return new UserCreationDto(username, password);
    }
}
