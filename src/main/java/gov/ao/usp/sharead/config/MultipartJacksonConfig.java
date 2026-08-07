package gov.ao.usp.sharead.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
public class MultipartJacksonConfig extends MappingJackson2HttpMessageConverter {
    
    public MultipartJacksonConfig(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        // Permite ler o DTO mesmo se vier como APPLICATION_OCTET_STREAM ou TEXT_PLAIN dentro do FormData
        return super.canRead(clazz, mediaType) || 
               MediaType.APPLICATION_OCTET_STREAM.isCompatibleWith(mediaType) ||
               MediaType.TEXT_PLAIN.isCompatibleWith(mediaType);
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(type, contextClass, mediaType) || 
               MediaType.APPLICATION_OCTET_STREAM.isCompatibleWith(mediaType) ||
               MediaType.TEXT_PLAIN.isCompatibleWith(mediaType);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) 
            throws IOException, HttpMessageNotReadableException {
        return super.readInternal(clazz, inputMessage);
    }
}