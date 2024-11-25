package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        int idExpected = dto.getId();
        int result = entity.getId();
        TaskPriorityEnum tpeExpected = dto.getDefaultTaskPriority();
        TaskPriorityEnum tpeResult = entity.getDefaultTaskPriority();
        TaskStatusEnum tseExpected = dto.getDefaultTaskStatus();
        TaskStatusEnum tseResult = entity.getDefaultTaskStatus();

        Assertions.assertEquals(idExpected, result);
        Assertions.assertEquals(tpeExpected, tpeResult);
        Assertions.assertEquals(tseExpected, tseResult);
    }

    @Test
    @DisplayName("Тест: Конвертация DefaultSettings в DTO")
    void testConvertDefaultSettingsToDto() {
        DefaultSettings entity = new DefaultSettings(1, null, TaskStatusEnum.BACKLOG, TaskPriorityEnum.HIGH);
        DefaultSettingsDto dto = mapper.convertDefaultSettingsToDto(entity);

        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getDefaultTaskPriority(), dto.getDefaultTaskPriority());
        Assertions.assertEquals(entity.getDefaultTaskStatus(), dto.getDefaultTaskStatus());
    }

    @Test
    @DisplayName("Тест: Сравнение DefaultSettings и DTO")
    void testCompareDefaultSettingsAndDto() {
        DefaultSettings entity = new DefaultSettings(1, null, TaskStatusEnum.PENDING, TaskPriorityEnum.LOW);
        DefaultSettingsDto dto = new DefaultSettingsDto(1, null, TaskStatusEnum.BACKLOG, null);

        DefaultSettings updatedEntity = mapper.compareDefaultSettingsAndDto(dto, entity);

        Assertions.assertNotEquals(dto.getDefaultTaskPriority(), updatedEntity.getDefaultTaskPriority());
        Assertions.assertEquals(entity.getDefaultTaskStatus(), updatedEntity.getDefaultTaskStatus());
    }
}
