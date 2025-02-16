package org.example.mhcommon.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonMapper {
    /*
    Object Mapper la class chinh cua Jackson de chuyen doi JSON <-> Object
    static nghia la chi co 1 instance duy nhat trong toan bo chuong trinh
     */
    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) new JsonMapper().resetJsonConfig(); //goi resetJsonConfig de khoi tao
        return objectMapper;
    }

    public void resetJsonConfig() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}