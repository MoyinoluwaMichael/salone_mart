package africa.techimmortal.martbackend.core.utils;

import africa.techimmortal.martbackend.infrastructure.exception.MapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MartMapper {
    private final ObjectMapper objectMapper;

    public MartMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String writeValueAsString(Object object) throws MapperException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error writing Value As String{}", e.getMessage());
            throw new MapperException(e.getMessage());
        }
    }

    public <T> T readValue(String content, Class<T> valueType) throws MapperException {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            log.error("Error writing Value As String{}", e.getMessage());
            throw new MapperException(e.getMessage());
        }
    }

    public <T> T readValue(Object content, Class<T> valueType) throws MapperException {
        try {
            String contentAsString = objectMapper.writeValueAsString(content);
            return objectMapper.readValue(contentAsString, valueType);
        } catch (JsonProcessingException e) {
            log.error("Error writing Value As String{}", e.getMessage());
            throw new MapperException(e.getMessage());
        }
    }

    public <T> List<T> readValue(String content, TypeReference<List<T>> typeReference) throws MapperException {
        try {
            return objectMapper.readValue(content, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error reading value as List: {}", e.getMessage());
            throw new MapperException(e.getMessage());
        }
    }
}
