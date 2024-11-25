package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.mapper.DefaultSettingsMapper;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MappersFabricImplTest {

    private MappersFabricImpl mappersFabric;
    private NotesMapper notesMapper;
    private TaskMapper taskMapper;
    private UserMapper userMapper;
    private DefaultSettingsMapper defaultSettingsMapper;

    @BeforeEach
    void setUp() {
        notesMapper = Mockito.mock(NotesMapper.class);
        taskMapper = Mockito.mock(TaskMapper.class);
        userMapper = Mockito.mock(UserMapper.class);
        defaultSettingsMapper = Mockito.mock(DefaultSettingsMapper.class);

        mappersFabric = new MappersFabricImpl(notesMapper, taskMapper, userMapper, defaultSettingsMapper);
    }

    @Test
    @DisplayName("Тест: Проверка создания NotesMapper")
    void testCreateNotesMapper() {
        NotesMapper result = mappersFabric.createNotesMapper();
        Assertions.assertNotNull(result, "NotesMapper не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания TaskMapper")
    void testCreateTaskMapper() {
        TaskMapper result = mappersFabric.createTaskMapper();
        Assertions.assertNotNull(result, "TaskMapper не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания UserMapper")
    void testCreateUserMapper() {
        UserMapper result = mappersFabric.createUserMapper();
        Assertions.assertNotNull(result, "UserMapper не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания DefaultSettingsMapper")
    void testCreateDefaultSettingsMapper() {
        DefaultSettingsMapper result = mappersFabric.createDefaultSettingsMapper();
        Assertions.assertNotNull(result, "DefaultSettingsMapper не должен быть null");
    }
}
