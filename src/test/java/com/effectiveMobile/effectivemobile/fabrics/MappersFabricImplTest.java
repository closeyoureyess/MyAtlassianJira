package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.mapper.DefaultSettingsMapper;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class MappersFabricImplTest {

    private MappersFabricImpl mappersFabric;
    private NotesMapper notesMapper;
    private TaskMapper taskMapper;
    private UserMapper userMapper;
    private DefaultSettingsMapper defaultSettingsMapper;

    @BeforeEach
    void setUp() {
        notesMapper = mock(NotesMapper.class);
        taskMapper = mock(TaskMapper.class);
        userMapper = mock(UserMapper.class);
        defaultSettingsMapper = mock(DefaultSettingsMapper.class);

        mappersFabric = new MappersFabricImpl(notesMapper, taskMapper, userMapper, defaultSettingsMapper);
    }

    @Test
    @DisplayName("Тест: Проверка создания NotesMapper")
    void testCreateNotesMapper() {
        assertNotNull(mappersFabric.createNotesMapper(), "NotesMapper не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания TaskMapper")
    void testCreateTaskMapper() {
        assertNotNull(mappersFabric.createTaskMapper(), "TaskMapper не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания UserMapper")
    void testCreateUserMapper() {
        assertNotNull(mappersFabric.createUserMapper(), "UserMapper не должен быть null");
    }

    @Test
    @DisplayName("Тест: Проверка создания DefaultSettingsMapper")
    void testCreateDefaultSettingsMapper() {
        assertNotNull(mappersFabric.createDefaultSettingsMapper(), "DefaultSettingsMapper не должен быть null");
    }
}
