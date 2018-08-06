package com.xiaowei.expensereimbursementweb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by yuanxuan on 2018/3/26.
 */
@Configuration
public class JsonConfig {

    @Bean
    @SuppressWarnings("all")
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>()
        {

            @Override
            public void serialize(
                    Object value,
                    JsonGenerator jg,
                    SerializerProvider sp) throws IOException, JsonProcessingException
            {
                jg.writeNull();
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(simpleDateFormat);

        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mappingJackson2HttpMessageConverter;
    }
}
