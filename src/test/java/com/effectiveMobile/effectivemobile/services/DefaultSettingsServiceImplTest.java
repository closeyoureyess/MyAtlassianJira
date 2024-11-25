package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.DefaultSettingsDto;
import com.effectiveMobile.effectivemobile.entities.DefaultSettings;
import com.effectiveMobile.effectivemobile.exeptions.IncorrectTypeParameterException;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.mapper.DefaultSettingsMapper;
import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import com.effectiveMobile.effectivemobile.repository.DefaultSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultSettingsServiceImplTest {

    private DefaultSettingsServiceImpl defaultSettingsService;
    private DefaultSettingsRepository mockRepository;
    private MappersFabric mockMappersFabric;
    private DefaultSettingsMapper mockMapper;

    @BeforeEach
    void setUp() {
        mockRepository = Mockito.mock(DefaultSettingsRepository.class);
        mockMappersFabric = Mockito.mock(MappersFabric.class);
        mockMapper = Mockito.mock(DefaultSettingsMapper.class);

        defaultSettingsService = new DefaultSettingsServiceImpl();
        defaultSettingsService.setDefaultSettingsRepository(mockRepository);
        defaultSettingsService.setMappersFabric(mockMappersFabric);

        when(mockMappersFabric.createDefaultSettingsMapper()).thenReturn(mockMapper);
    }

    @Test
    @DisplayName("Тест: Успешное изменение настроек")
    void testChangeDefaultSettingsSuccess() throws IncorrectTypeParameterException {
        // Входные данные
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, null,
                TaskPriorityEnum.HIGH);
        DefaultSettings entity = new DefaultSettings();
        entity.setFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        entity.setDefaultTaskPriority(TaskPriorityEnum.MEDIUM);

        // Мокаем зависимости
        when(mockRepository.findByFieldName("TASK_PRIORITY")).thenReturn(Optional.of(entity)); // Симулируем успешный поиск в БД
        when(mockMapper.compareDefaultSettingsAndDto(dto, entity)).thenReturn(entity); // Симулируем сравнение и обновление сущности
        when(mockRepository.save(entity)).thenReturn(entity); // Симулируем сохранение в БД
        when(mockMapper.convertDefaultSettingsToDto(entity)).thenReturn(dto); // Симулируем маппинг обратно в DTO

        // Вызов тестируемого метода
        Optional<DefaultSettingsDto> result = defaultSettingsService.changeDefaultSettings(dto);

        // Проверки
        assertTrue(result.isPresent(), "Результат должен быть непустым");
        assertEquals(dto, result.get(), "Результат должен совпадать с DTO");
        verify(mockRepository, times(1)).findByFieldName("TASK_PRIORITY");
        verify(mockMapper, times(1)).compareDefaultSettingsAndDto(dto, entity);
        verify(mockRepository, times(1)).save(entity);
        verify(mockMapper, times(1)).convertDefaultSettingsToDto(entity);
    }


    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - оба поля заполнены")
    void testChangeDefaultSettingsTooManyFieldsException() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG, TaskPriorityEnum.HIGH);

        IncorrectTypeParameterException exception = assertThrows(IncorrectTypeParameterException.class, () -> defaultSettingsService.changeDefaultSettings(dto));
        assertEquals("Слишком много полей в одном запросе", exception.getMessage());
    }

    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - поле пустое")
    void testChangeDefaultSettingsNullEditableValuesException() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, null, null);

        IncorrectTypeParameterException exception = assertThrows(IncorrectTypeParameterException.class, () -> defaultSettingsService.changeDefaultSettings(dto));
        assertEquals("Значения редактируемых полей не могут быть пустыми", exception.getMessage());
    }

    @Test
    @DisplayName("Тест: Успешное получение настроек")
    void testGetDefaultSettingsSuccess() {
        DefaultSettingsFieldNameEnum fieldName = DefaultSettingsFieldNameEnum.TASK_PRIORITY;
        DefaultSettings entity = new DefaultSettings();
        DefaultSettingsDto dto = new DefaultSettingsDto();

        when(mockRepository.findByFieldName("TASK_PRIORITY")).thenReturn(Optional.of(entity));
        when(mockMapper.convertDefaultSettingsToDto(entity)).thenReturn(dto);

        Optional<DefaultSettingsDto> result = defaultSettingsService.getDefaultSettings(fieldName);

        assertTrue(result.isPresent(), "Результат должен быть непустым");
        assertEquals(dto, result.get(), "Результат должен совпадать с DTO");
        verify(mockRepository, times(1)).findByFieldName("TASK_PRIORITY");
        verify(mockMapper, times(1)).convertDefaultSettingsToDto(entity);
    }

    @Test
    @DisplayName("Тест: Получение настроек - поле отсутствует в БД")
    void testGetDefaultSettingsNotFound() {
        DefaultSettingsFieldNameEnum fieldName = DefaultSettingsFieldNameEnum.TASK_PRIORITY;

        when(mockRepository.findByFieldName("TASK_PRIORITY")).thenReturn(Optional.empty());

        Optional<DefaultSettingsDto> result = defaultSettingsService.getDefaultSettings(fieldName);

        assertTrue(result.isEmpty(), "Результат должен быть пустым");
        verify(mockRepository, times(1)).findByFieldName("TASK_PRIORITY");
        verify(mockMapper, never()).convertDefaultSettingsToDto(any());
    }
}

