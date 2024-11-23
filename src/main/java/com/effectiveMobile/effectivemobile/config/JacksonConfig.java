package com.effectiveMobile.effectivemobile.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // Создаём провайдер фильтров
            SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                    // Фильтр для TasksDto, который сериализует все поля
                    .addFilter("TasksDtoFilter", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("CustomUsersDtoFilter", SimpleBeanPropertyFilter.serializeAll())
                    .addFilter("NotesDtoFilter", SimpleBeanPropertyFilter.serializeAll())
                    // Устанавливаем, что отсутствие фильтра не должно приводить к ошибкам
                    /*.addFilter("TasksDtoNotesControllerFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id"))
                    .addFilter("CustomUsersDtoNotesControllerFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "email"))
                    .addFilter("NotesDtoNotesControllerFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "usersDto", "comments",
                            "task"))

                    .addFilter("TasksDtoTaskControllerFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id"))*/
                    // Устанавливаем, что отсутствие фильтра не должно приводить к ошибкам
                    .setFailOnUnknownId(false);

            builder.filters(filterProvider);
        };
    }

}
