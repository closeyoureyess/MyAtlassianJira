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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.*;

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

        Mockito.when(mockMappersFabric.createDefaultSettingsMapper()).thenReturn(mockMapper);
    }

    @Test
    @DisplayName("Optional<DefaultSettings> пуст, возвращен Optional.empty()")
    void testChangeDefaultSettingsIfOptionalDefSettingsEmpty() throws IncorrectTypeParameterException {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, null,
                TaskPriorityEnum.HIGH);
        DefaultSettings entity = new DefaultSettings();
        entity.setFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        entity.setDefaultTaskPriority(TaskPriorityEnum.MEDIUM);

        Mockito.when(mockRepository.findByFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY)).thenReturn(Optional.empty());

        Optional<DefaultSettingsDto> result = defaultSettingsService.changeDefaultSettings(dto);

        Assertions.assertTrue(result.isEmpty(), "Результат должен быть пустой");
    }

    @Test
    @DisplayName("Тест: Успешное изменение настроек")
    void testChangeDefaultSettingsSuccess() throws IncorrectTypeParameterException {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, null,
                TaskPriorityEnum.HIGH);
        DefaultSettings entity = new DefaultSettings();
        entity.setFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        entity.setDefaultTaskPriority(TaskPriorityEnum.MEDIUM);

        Mockito.when(mockRepository.findByFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY)).thenReturn(Optional.of(entity)); // Симулируем успешный поиск в БД
        Mockito.when(mockMapper.compareDefaultSettingsAndDto(dto, entity)).thenReturn(entity); // Симулируем сравнение и обновление сущности
        Mockito.when(mockRepository.save(entity)).thenReturn(entity); // Симулируем сохранение в БД
        Mockito.when(mockMapper.convertDefaultSettingsToDto(entity)).thenReturn(dto); // Симулируем маппинг обратно в DTO

        Optional<DefaultSettingsDto> result = defaultSettingsService.changeDefaultSettings(dto);

        Assertions.assertTrue(result.isPresent(), "Результат должен быть непустым");
        Assertions.assertEquals(dto, result.get(), "Результат должен совпадать с DTO");
        Mockito.verify(mockRepository, Mockito.times(1)).findByFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        Mockito.verify(mockMapper, Mockito.times(1)).compareDefaultSettingsAndDto(dto, entity);
        Mockito.verify(mockRepository, Mockito.times(1)).save(entity);
        Mockito.verify(mockMapper, Mockito.times(1)).convertDefaultSettingsToDto(entity);
    }

    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - оба поля заполнены")
    void testChangeDefaultSettingsTooManyFieldsException() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG, TaskPriorityEnum.HIGH);

        IncorrectTypeParameterException exception = Assertions.assertThrows(IncorrectTypeParameterException.class, () -> defaultSettingsService.changeDefaultSettings(dto));
        Assertions.assertEquals(TOO_MANY_FIELDS_IN_ONE_REQUEST.getEnumDescription(), exception.getMessage());
    }

    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - поле пустое")
    void testChangeDefaultSettingsNullEditableValuesException() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, null, null);

        IncorrectTypeParameterException exception = Assertions.assertThrows(IncorrectTypeParameterException.class, () -> defaultSettingsService.changeDefaultSettings(dto));
        Assertions.assertEquals(VALUES_EDITABLE_FIELDS_NOT_BE_NULL.getEnumDescription(), exception.getMessage());
    }

    @Test
    @DisplayName("Тест: Успешное получение настроек")
    void testGetDefaultSettingsSuccess() {
        DefaultSettingsFieldNameEnum fieldName = DefaultSettingsFieldNameEnum.TASK_PRIORITY;
        DefaultSettings entity = new DefaultSettings();
        DefaultSettingsDto dto = new DefaultSettingsDto();

        Mockito.when(mockRepository.findByFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY)).thenReturn(Optional.of(entity));
        Mockito.when(mockMapper.convertDefaultSettingsToDto(entity)).thenReturn(dto);

        Optional<DefaultSettingsDto> result = defaultSettingsService.getDefaultSettings(fieldName);

        Assertions.assertTrue(result.isPresent(), "Результат должен быть непустым");
        Assertions.assertEquals(dto, result.get(), "Результат должен совпадать с DTO");
        Mockito.verify(mockRepository, Mockito.times(1)).findByFieldName(DefaultSettingsFieldNameEnum.TASK_PRIORITY);
        Mockito.verify(mockMapper, Mockito.times(1)).convertDefaultSettingsToDto(entity);
    }

    @Test
    @DisplayName("Тест: Получение настроек - поле отсутствует в БД")
    void testGetDefaultSettingsNotFound() {
        DefaultSettingsFieldNameEnum fieldName = DefaultSettingsFieldNameEnum.TASK_PRIORITY;

        Mockito.when(mockRepository.findByFieldName(fieldName)).thenReturn(Optional.empty());

        Optional<DefaultSettingsDto> result = defaultSettingsService.getDefaultSettings(fieldName);

        Assertions.assertTrue(result.isEmpty(), "Результат должен быть пустым");
        Mockito.verify(mockRepository, Mockito.times(1)).findByFieldName(fieldName);
        Mockito.verify(mockMapper, Mockito.never()).convertDefaultSettingsToDto(Mockito.any());
    }

    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - TASK_PRIORITY не может быть null")
    void testChangeDefaultSettingsTaskPriorityNotNull() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, null, null);

        IncorrectTypeParameterException exception = Assertions.assertThrows(IncorrectTypeParameterException.class, () -> defaultSettingsService.changeDefaultSettings(dto));
        Assertions.assertEquals(VALUES_EDITABLE_FIELDS_NOT_BE_NULL.getEnumDescription(), exception.getMessage());
    }

    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - TASK_PRIORITY и BACKLOG")
    void testChangeDefaultSettingsTaskPriorityAndBacklog() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_PRIORITY, TaskStatusEnum.BACKLOG,
                null);

        IncorrectTypeParameterException exception = Assertions.assertThrows(IncorrectTypeParameterException.class, () ->
                defaultSettingsService.changeDefaultSettings(dto));
        Assertions.assertEquals(TASK_PRIORITY_NOT_BE_NULL.getEnumDescription(), exception.getMessage());
    }

    @Test
    @DisplayName("Тест: Изменение настроек с некорректными параметрами - TASK_STATUS не может быть null")
    void testChangeDefaultSettingsTaskStatusAndTaskPriority() {
        DefaultSettingsDto dto = new DefaultSettingsDto(1, DefaultSettingsFieldNameEnum.TASK_STATUS, null,
                TaskPriorityEnum.HIGHEST);

        IncorrectTypeParameterException exception = Assertions.assertThrows(IncorrectTypeParameterException.class, () ->
                defaultSettingsService.changeDefaultSettings(dto));
        Assertions.assertEquals(SELECTED_TASK_STATUS_IS_NULL.getEnumDescription(), exception.getMessage());
    }
}