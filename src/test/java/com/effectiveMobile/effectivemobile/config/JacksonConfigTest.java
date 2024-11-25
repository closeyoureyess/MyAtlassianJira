package com.effectiveMobile.effectivemobile.config;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JacksonConfigTest {

    private final JacksonConfig jacksonConfig = new JacksonConfig();

    @Test
    @DisplayName("Проверка наличия кастомайзера Jackson")
    void testJacksonCustomizerNotNull() {
        // Act
        Jackson2ObjectMapperBuilderCustomizer customizer = jacksonConfig.jacksonCustomizer();

        // Assert
        assertNotNull(customizer, "Customizer должен быть создан и не равен null");
    }

    @Test
    @DisplayName("Проверка настройки фильтров через Jackson Customizer")
    void testJacksonCustomizerFilters() throws JsonProcessingException {
        // Arrange
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        // Act
        Jackson2ObjectMapperBuilderCustomizer customizer = jacksonConfig.jacksonCustomizer();
        customizer.customize(builder);

        ObjectMapper objectMapper = builder.build();

        FilterProvider filterProvider = objectMapper.getSerializationConfig().getFilterProvider();

        // Assert
        assertNotNull(filterProvider, "FilterProvider должен быть создан");

        // Проверяем, что фильтры работают на примере сериализации
        TestDto testDto = new TestDto("value1", "value2");
        String json = objectMapper.writeValueAsString(testDto);

        // Проверяем, что сериализация происходит без ошибок, и содержимое корректное
        assertTrue(json.contains("field1"), "Сериализация должна содержать field1");
        assertTrue(json.contains("field2"), "Сериализация должна содержать field2");
    }

    @JsonFilter("TasksDtoFilter")
    static class TestDto {
        private final String field1;
        private final String field2;

        public TestDto(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }
}
