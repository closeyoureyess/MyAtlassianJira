package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DefaultSettingsMapperImplTest {

    private DefaultSettingsMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new DefaultSettingsMapperImpl();
    }

    @Test
    @DisplayName("Тест: Конвертация DTO в DefaultSettings")
    void testConvertDtoToDefaultSettings() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, null, TaskStatusEnum.BACKLOG, TaskPriorityEnum.HIGH);
        DefaultSettings entity = mapper.convertDtoToDefaultSettings(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getDefaultTaskPriority(), entity.getDefaultTaskPriority());
        assertEquals(dto.getDefaultTaskStatus(), entity.getDefaultTaskStatus());
    }

    @Test
    @DisplayName("Тест: Конвертация DefaultSettings в DTO")
    void testConvertDefaultSettingsToDto() {
        DefaultSettings entity = new DefaultSettings(1, null, TaskStatusEnum.BACKLOG, TaskPriorityEnum.HIGH);
        DefaultSettingsDto dto = mapper.convertDefaultSettingsToDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getDefaultTaskPriority(), dto.getDefaultTaskPriority());
        assertEquals(entity.getDefaultTaskStatus(), dto.getDefaultTaskStatus());
    }

    @Test
    @DisplayName("Тест: Сравнение DefaultSettings и DTO")
    void testCompareDefaultSettingsAndDto() {
        DefaultSettings entity = new DefaultSettings(1, null,  TaskStatusEnum.PENDING, TaskPriorityEnum.LOW);
        DefaultSettingsDto dto = new DefaultSettingsDto(1, null, TaskStatusEnum.BACKLOG, null);

        DefaultSettings updatedEntity = mapper.compareDefaultSettingsAndDto(dto, entity);

        assertNotEquals(dto.getDefaultTaskPriority(), updatedEntity.getDefaultTaskPriority());
        assertEquals(entity.getDefaultTaskStatus(), updatedEntity.getDefaultTaskStatus());
    }
}
