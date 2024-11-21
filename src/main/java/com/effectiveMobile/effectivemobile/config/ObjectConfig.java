package com.effectiveMobile.effectivemobile.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectConfig {

    @Bean
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

}
