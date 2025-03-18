package africa.springCore.martbackend.common.utils;

import africa.springCore.martbackend.infrastructure.exception.MapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class MartMapper {
    private final ObjectMapper objectMapper;


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
}
