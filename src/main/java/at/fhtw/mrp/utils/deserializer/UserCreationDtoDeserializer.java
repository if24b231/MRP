package at.fhtw.mrp.utils.deserializer;

import at.fhtw.mrp.model.UserCreationDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class UserCreationDtoDeserializer extends StdDeserializer<UserCreationDto> {

    public UserCreationDtoDeserializer() {
        this(null);
    }

    public UserCreationDtoDeserializer(Class<?> vc) {
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
